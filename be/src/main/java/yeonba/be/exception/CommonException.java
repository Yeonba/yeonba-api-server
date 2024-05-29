package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonException implements BaseException {

    BAD_REQUEST(
        HttpStatus.BAD_REQUEST,
        "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "서버 에러 관리자에게 문의 바랍니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
