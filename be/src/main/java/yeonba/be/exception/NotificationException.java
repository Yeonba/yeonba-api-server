package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotificationException implements BaseException {

    NOTIFICATION_PERMISSION_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 알림 동의 내역이 존재하지 않습니다."),

    DEVICE_TOKEN_NOT_FOUND(
        HttpStatus.BAD_REQUEST,
        "해당 사용자의 device token이 존재하지 않습니다. token을 먼저 등록해주세요."),

    REQUEST_PERMISSIONS_CAN_NOT_CONTAIN_NULL(
        HttpStatus.BAD_REQUEST,
        "요청되는 동의 내역들엔 null 값이 포함될 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
