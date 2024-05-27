package yeonba.be.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    public long getUserIdFromJwt(String token) {

        Object userIdObject = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
            .get("userId");

        if (userIdObject instanceof String) {
            return Long.parseLong((String) userIdObject);
        } else if (userIdObject instanceof Integer) {
            return ((Integer) userIdObject).longValue();
        } else {
            throw new IllegalArgumentException(
                "Unexpected type for userId: " + userIdObject.getClass().getName());
        }
    }

    private Date getExpiredAt(
        Date generatedAt, Duration duration) {

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

    public boolean validateToken(String token) {

        try {
            byte[] decodedSecretKey = Base64.getDecoder().decode(jwtSecret);
            Key key = new SecretKeySpec(decodedSecretKey, 0, decodedSecretKey.length, "HmacSHA256");

            Jwts.parser()
                .setSigningKey(key) // 비밀 키를 사용하여 서명을 검증
                .parseClaimsJws(token);

            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
