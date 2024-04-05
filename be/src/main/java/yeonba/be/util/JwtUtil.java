package yeonba.be.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UtilException;
import yeonba.be.user.entity.User;

@Component
public class JwtUtil {

    private final Duration ACCESS_TOKEN_DURATION = Duration.of(8, ChronoUnit.HOURS);
    private final Duration REFRESH_TOKEN_DURATION = Duration.of(10, ChronoUnit.DAYS);

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateAccessToken(User user, Date generatedAt) {

        Date expiredAt = getExpiredAt(generatedAt, ACCESS_TOKEN_DURATION);

        return generateUserJwt(user, generatedAt, expiredAt);
    }

    public String generateRefreshToken(User user, Date generatedAt) {

        Date expiredAt = getExpiredAt(generatedAt, REFRESH_TOKEN_DURATION);

        return generateUserJwt(user, generatedAt, expiredAt);
    }

    private Date getExpiredAt(Date generatedAt, Duration duration) {

        Instant instant = generatedAt.toInstant()
            .plusMillis(duration.toMillis());

        return Date.from(instant);
    }

    private String generateUserJwt(User user, Date issuedAt, Date generatedAt) {

        return Jwts.builder()
            .setIssuedAt(issuedAt)
            .setExpiration(generatedAt)
            .claim("userId", user.getId())
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
    }

    public long parseUserIdFromJwt(String jwt) {

        validateJwt(jwt);

        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(jwt)
            .getBody()
            .get("userId", Long.class);
    }

    private void validateJwt(String jwt) {

        try {
            Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt);

        } catch (Exception e) {

            throw new GeneralException(UtilException.INVALID_JWT);
        }
    }
}
