package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BlockException implements BaseException {

    ALREADY_BLOCKED_USER(
        HttpStatus.BAD_REQUEST,
        "이미 차단한 사용자입니다."),

    NOT_BLOCKED_USER(
        HttpStatus.BAD_REQUEST,
        "차단한 사용자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
