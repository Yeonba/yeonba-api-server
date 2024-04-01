package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum AcquaintanceException implements BaseException {


    ALREADY_EXIST_PHONENUMBER(
        HttpStatus.BAD_REQUEST,
        "중복된 phone number가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String reason;

    AcquaintanceException(HttpStatus httpStatus, String reason) {
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
