package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum UserException implements BaseException {

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

    USER_PREFERENCE_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 사용자의 선호내역이 존재하지 않습니다."),

    IS_NOT_ADULT(
        HttpStatus.BAD_REQUEST,
        "만 18세 미만은 저희 서비스를 이용할 수 없습니다."),

    LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND(
        HttpStatus.BAD_REQUEST,
        "하한 값은 상한 값보다 작거나 같아야 합니다.");

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
