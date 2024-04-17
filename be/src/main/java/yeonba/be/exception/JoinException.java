package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum JoinException implements BaseException {

    PASSWORD_CONFIRMATION_NOT_MATCH(
        HttpStatus.BAD_REQUEST,
        "비밀번호 확인 값이 비밀번호와 일치하지 않습니다."),

    ALREADY_USED_EMAIL(
        HttpStatus.BAD_REQUEST,
        "이미 사용 중인 이메일입니다."),

    ALREADY_USED_NICKNAME(
        HttpStatus.BAD_REQUEST,
        "이미 사용 중인 닉네임입니다."),

    ALREADY_USED_PHONE_NUMBER(
        HttpStatus.BAD_REQUEST,
        "이미 사용 중인 핸드폰 번호입니다.");

    private final HttpStatus httpStatus;
    private final String reason;

    JoinException(HttpStatus httpStatus, String reason) {

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
