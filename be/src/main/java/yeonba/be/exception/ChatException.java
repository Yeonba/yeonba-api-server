package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatException implements BaseException {

    NOT_FOUND_CHAT_ROOM(
        HttpStatus.BAD_REQUEST,
        "요청된 채팅방이 없습니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
