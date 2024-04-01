package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum JoinException implements BaseException {

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
