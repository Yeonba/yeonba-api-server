package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPhoneNumberVerifyRequest {

  @Schema(
      type = "string",
      description = "전화번호",
      example = "010-1111-2222"
  )
  private String phoneNumber;
}
