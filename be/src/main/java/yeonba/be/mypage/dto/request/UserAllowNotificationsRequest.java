package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.notification.enums.NotificationType;

@Getter
@NoArgsConstructor
public class UserAllowNotificationsRequest {

    @Schema(
        type = "boolean",
        description = "화살 받을 시 알림 동의 여부",
        example = "true")
    @NotNull(message = "화살 받을 시 알림 동의 여부는 필수 값입니다.")
    private boolean allowArrowReceivedNotification;

    @Schema(
        type = "boolean",
        description = "채팅 요청 받을 시 알림 동의 여부",
        example = "true")
    @NotNull(message = "채팅 요청 받을 시 알림 동의 여부는 필수 값입니다.")
    private boolean allowChattingRequestNotification;

    @Schema(
        type = "boolean",
        description = "요청한 채팅 수락 시 알림 동의 여부",
        example = "true")
    @NotNull(message = "요청한 채팅 수락 시 알림 동의 여부는 필수 값입니다.")
    private boolean allowChattingRequestAcceptedNotification;

    public Map<NotificationType, Boolean> toNotificationTypeToPermissionStatus() {

        Map<NotificationType, Boolean> notificationTypeToPermissionStatus = new HashMap<>();
        notificationTypeToPermissionStatus
            .put(NotificationType.ARROW_RECEIVED,
                this.allowArrowReceivedNotification);
        notificationTypeToPermissionStatus
            .put(NotificationType.CHATTING_REQUESTED,
                this.allowChattingRequestNotification);
        notificationTypeToPermissionStatus
            .put(NotificationType.CHATTING_REQUEST_ACCEPTED,
                this.allowChattingRequestAcceptedNotification);

        return notificationTypeToPermissionStatus;
    }
}
