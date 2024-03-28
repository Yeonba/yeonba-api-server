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
import yeonba.be.exception.ServiceJwtException;
import yeonba.be.user.entity.User;

@Component
public class JwtUtil {

    private final Duration ACCESS_TOKEN_DURATION = Duration.of(8, ChronoUnit.HOURS);
    private final Duration REFRESH_TOKEN_DURATION = Duration.of(10, ChronoUnit.DAYS);

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateAccessToken(User user, Date issuedAt) {

        Date expiredAt = getExpiredAt(issuedAt, ACCESS_TOKEN_DURATION);

        return generateUserJwt(user, issuedAt, expiredAt);
    }

    public String generateRefreshToken(User user, Date issuedAt) {

        Date expiredAt = getExpiredAt(issuedAt, REFRESH_TOKEN_DURATION);

        return generateUserJwt(user, issuedAt, expiredAt);
    }

    private Date getExpiredAt(Date issuedAt, Duration duration) {

        Instant instant = issuedAt.toInstant()
            .plusMillis(duration.toMillis());

        return Date.from(instant);
    }

    private String generateUserJwt(
        User user,
        Date issuedAt,
        Date expiredAt) {

        return Jwts.builder()
            .setSubject(user.getEmail())
            .setIssuedAt(issuedAt)
            .setExpiration(expiredAt)
            .claim("userId", user.getId())
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
    }

    public void validateJwt(String jwt) {

        try {
            Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt);

        } catch (Exception e) {
            ServiceJwtException exception = ServiceJwtException.from(e);
            throw new GeneralException(exception);
        }
    }

    public long parseUserIdFromJwt(String jwt) {

        validateJwt(jwt);

        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(jwt)
            .getBody()
            .get("userId", Long.class);
    }
}
