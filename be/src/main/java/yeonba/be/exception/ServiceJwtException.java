package yeonba.be.exception;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;

public enum ServiceJwtException implements BaseException {

    INVALID_FORMAT(
        HttpStatus.BAD_REQUEST,
        "유효하지 않은 형식의 토큰입니다."),

    INVALID_SIGNATURE(
        HttpStatus.BAD_REQUEST,
        "유효하지 않은 시그니처입니다."),

    EXPIRED(
        HttpStatus.BAD_REQUEST,
        "만료된 JWT입니다.");

    private final HttpStatus httpStatus;
    private final String reason;

    ServiceJwtException(HttpStatus httpStatus, String reason) {

        this.httpStatus = httpStatus;
        this.reason = reason;
    }

    @Override
    public HttpStatus getHttpStatus() {

        return httpStatus;
    }

    @Override
    public String getReason() {

        return reason;
    }

    public static ServiceJwtException from(Exception e) {

        if (e instanceof MalformedJwtException) {

            return INVALID_FORMAT;
        }

        if (e instanceof SignatureException) {

            return INVALID_SIGNATURE;
        }

        return EXPIRED;
    }
}
