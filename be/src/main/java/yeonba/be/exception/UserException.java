package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserException implements BaseException {

    INVALID_REFRESH_TOKEN(
        HttpStatus.UNAUTHORIZED,
        "유효하지 않은 리프레시 토큰입니다."),

    NOT_MATCH_LOGIN_TYPE(
        HttpStatus.BAD_REQUEST,
        "다른 로그인 방식을 이용해주세요."),

    VOCAL_RANGE_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "존재하지 않는 음역대입니다."),

    ANIMAL_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "존재하지 않는 동물상입니다."),

    AREA_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "존재하지 않는 지역입니다."),

    USER_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 사용자가 존재하지 않습니다."),

    NOT_INACTIVE_USER(
        HttpStatus.BAD_REQUEST,
        "휴면 상태 사용자가 아닙니다."),

    USER_PREFERENCE_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 사용자의 선호내역이 존재하지 않습니다."),

    AGE_OUT_OF_RANGE(
        HttpStatus.BAD_REQUEST,
        "서비스 이용이 가능한 사용자 나이는 20~40세입니다."),

    LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND(
        HttpStatus.BAD_REQUEST,
        "하한 값은 상한 값보다 작거나 같아야 합니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
