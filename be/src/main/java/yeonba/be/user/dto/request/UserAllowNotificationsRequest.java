package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAllowNotificationsRequest {

  @Schema(
      type = "boolean",
      description = "화살 받을 시 알림 허용 여부",
      example = "true"
  )
  private Boolean allowReceivedArrowNotification;
  @Schema(
      type = "boolean",
      description = "채팅 요청 받을 시 알림 허용 여부",
      example = "true"
  )
  private Boolean allowChattingRequestNotification;
}
