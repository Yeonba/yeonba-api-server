package yeonba.be.mypage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.notification.entity.NotificationPermission;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPermissionDetail {

    @Schema(
        type = "string",
        description = "알림 유형",
        example = "ARROW_RECEIVED")
    @Pattern(
        regexp = "^(ARROW_RECEIVED|CHATTING_REQUESTED|CHATTING_REQUEST_ACCEPTED)$",
        message = "알림 유형은 허용된 문자열만 가능합니다.")
    @NotNull(message = "알림 유형은 반드시 입력되어야 합니다.")
    private String type;

    @Schema(
        type = "boolean",
        description = "알림 동의 여부",
        example = "true")
    @JsonProperty("isPermit")
    @NotNull(message = "알림 동의 여부는 반드시 입력되어야 합니다.")
    private boolean permit;

    public static NotificationPermissionDetail of(NotificationPermission notificationPermission) {

        return new NotificationPermissionDetail(
            notificationPermission.getType().name(),
            notificationPermission.getPermissionStatus());
    }
}
