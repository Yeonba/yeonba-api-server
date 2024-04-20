package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ArrowException implements BaseException {


    EXCEEDED_DAILY_AD_VIEWS(
        HttpStatus.BAD_REQUEST,
        "1일 광고 시청은 최대 3회입니다."),

    ALREADY_CHECKED_USER(
        HttpStatus.BAD_REQUEST,
        "이미 출석 체크한 사용자입니다."),

    ALREADY_SENT_ARROW_USER(
        HttpStatus.BAD_REQUEST,
        "이미 화살을 보낸 사용자입니다."),

    NOT_ENOUGH_ARROW_TO_SEND(
        HttpStatus.BAD_REQUEST,
        "화살이 부족하여 화살을 보낼 수 없습니다."),

    CAN_NOT_SEND_ARROW_TO_INACTIVE_USER(
        HttpStatus.BAD_REQUEST,
        "휴면 상태인 사용자에겐 화살을 보낼 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
