package yeonba.be.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationUnreadExistResponse {

    @Schema(
        type = "boolean",
        description = "읽지 않은 알림 존재 여부",
        example = "true")
    private boolean exist;
}
