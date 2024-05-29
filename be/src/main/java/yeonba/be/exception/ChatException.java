package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatException implements BaseException {

    NOT_YOUR_CHAT_ROOM(
        HttpStatus.BAD_REQUEST,
        "해당 채팅방에 대한 권한이 없습니다."),

    NOT_FOUND_CHAT_ROOM(
        HttpStatus.BAD_REQUEST,
        "요청된 채팅방이 없습니다."),

    ALREADY_CHAT_USER(
        HttpStatus.BAD_REQUEST,
        "이미 채팅 중인 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
