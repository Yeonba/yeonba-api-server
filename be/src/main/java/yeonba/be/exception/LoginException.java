package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum LoginException implements BaseException {

    VERIFICATION_CODE_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 인증 코드 내역이 존재하지 않습니다."),

    VERIFICATION_CODE_NOT_MATCH(
        HttpStatus.BAD_REQUEST,
        "인증 코드가 일치하지 않습니다."),

    PASSWORD_NOT_MATCH(
        HttpStatus.BAD_REQUEST,
        "비밀번호가 일치하지 않습니다."),

    REFRESH_TOKEN_NOT_MATCH(
        HttpStatus.BAD_REQUEST,
        "refresh token이 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String reason;

    LoginException(HttpStatus httpStatus, String reason) {

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
