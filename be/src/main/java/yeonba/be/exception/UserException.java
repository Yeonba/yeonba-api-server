package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum UserException implements BaseException {

    USER_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 사용자가 존재하지 않습니다."),

    NO_MORE_USERS_TO_RECOMMEND(
        HttpStatus.BAD_REQUEST,
        "더 이상 추천할 사용자가 존재하지 않습니다");

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
