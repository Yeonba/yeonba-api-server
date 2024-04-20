package yeonba.be.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationPermissionsResponse {

    @Schema(
        type = "boolean",
        description = "화살 받음 알림 허용 여부",
        example = "true")
    private boolean arrowReceivedNotificationPermission;

    @Schema(
        type = "boolean",
        description = "채팅 요청 알림 허용 여부",
        example = "true")
    private boolean chattingRequestNotificationPermission;

    @Schema(
        type = "boolean",
        description = "채팅 요청 수락 알림 허용 여부",
        example = "true")
    private boolean chattingRequestAcceptedNotificationPermission;
}
