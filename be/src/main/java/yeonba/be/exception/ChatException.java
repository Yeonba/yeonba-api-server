package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum ChatException implements BaseException {

    NOT_YOUR_CHAT_ROOM(
        HttpStatus.BAD_REQUEST,
        "해당 채팅방에 대한 권한이 없습니다."),

    NOT_FOUND_CHAT_ROOM(
        HttpStatus.BAD_REQUEST,
        "요청된 채팅방이 없습니다.");

    private final HttpStatus httpStatus;
    private final String reason;

    ChatException(HttpStatus httpStatus, String reason) {
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
