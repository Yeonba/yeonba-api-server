package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum UserException implements BaseException {

    INVALID_REFRESH_TOKEN(
        HttpStatus.UNAUTHORIZED,
        "유효하지 않은 리프레시 토큰입니다."),

    USER_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 사용자가 존재하지 않습니다."),

    NOT_MATCH_LOGIN_TYPE(
        HttpStatus.BAD_REQUEST,
        "다른 로그인 방식을 이용해주세요.");

    private final HttpStatus httpStatus;
    private final String reason;

    UserException(HttpStatus httpStatus, String reason) {
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
}
