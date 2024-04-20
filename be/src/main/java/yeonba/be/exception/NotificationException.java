package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationException implements BaseException {

    NOTIFICATION_PERMISSION_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 알림 동의 내역이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
