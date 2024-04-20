package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserException implements BaseException {

    SAME_USER(
        HttpStatus.BAD_REQUEST,
        "같은 사용자(자기 자신) 입니다."),

    SAME_GENDER_USER(
        HttpStatus.BAD_REQUEST,
        "같은 성별 사용자 입니다."),

    USER_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 사용자가 존재하지 않습니다."),

    INACTIVE_USER(
        HttpStatus.BAD_REQUEST,
        "휴면 상태 사용자입니다. 휴면 해제가 필요합니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
