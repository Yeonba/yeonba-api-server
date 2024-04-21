package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LoginException implements BaseException {

    VERIFICATION_CODE_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 인증 코드 내역이 존재하지 않습니다."),

    UNAUTHORIZED(
        HttpStatus.UNAUTHORIZED,
        "인증되지 않은 사용자입니다. 로그인이 필요합니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
