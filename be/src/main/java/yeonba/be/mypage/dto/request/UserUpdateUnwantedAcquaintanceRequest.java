package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateUnwantedAcquaintanceRequest {

  @Schema(
      type = "string",
      description = "이름",
      example = "안민재"
  )
  private String name;

  @Schema(
      type = "string",
      description = "전화번호, 0101111222 포맷",
      example = "01011112222"
  )
  @Pattern(regexp = "^010[0-9]{6}$")
  private String phoneNumber;
}
