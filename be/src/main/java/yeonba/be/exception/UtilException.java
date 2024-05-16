package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UtilException implements BaseException {

    INVALID_JWT(
        HttpStatus.BAD_REQUEST,
        "유효하지 않은 JWT입니다. 다시 로그인 해주세요."),

    NOT_ALLOWED_IMAGE_FILE_EXTENSION(
        HttpStatus.BAD_REQUEST,
        "jpg, jpeg, png 확장자 형식의 파일만 허용됩니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
