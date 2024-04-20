package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAllowNotificationsRequest {

    @Schema(
        type = "boolean",
        description = "화살 받을 시 알림 동의 여부",
        example = "true")
    @NotNull(message = "화살 받을 시 알림 동의 여부는 필수 값입니다.")
    private Boolean allowArrowReceivedNotification;

    @Schema(
        type = "boolean",
        description = "채팅 요청 받을 시 알림 동의 여부",
        example = "true")
    @NotNull(message = "채팅 요청 받을 시 알림 동의 여부는 필수 값입니다.")
    private Boolean allowChattingRequestNotification;

    @Schema(
        type = "boolean",
        description = "요청한 채팅 수락 시 알림 동의 여부",
        example = "true")
    @NotNull(message = "요청한 채팅 수락 시 알림 동의 여부는 필수 값입니다.")
    private Boolean allowChattingRequestAcceptedNotification;
}
