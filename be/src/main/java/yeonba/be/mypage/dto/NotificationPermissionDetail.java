package yeonba.be.mypage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.notification.entity.NotificationPermission;
import yeonba.be.notification.enums.NotificationType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPermissionDetail {

    @Schema(
        type = "string",
        description = "알림 유형",
        example = "ARROW_RECEIVED")
    private NotificationType type;

    @Schema(
        type = "boolean",
        description = "알림 동의 여부",
        example = "true")
    @JsonProperty("isPermit")
    private boolean permit;

    public static NotificationPermissionDetail of(NotificationPermission notificationPermission) {

        return new NotificationPermissionDetail(
            notificationPermission.getType(),
            notificationPermission.getPermissionStatus());
    }
}
