package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

  @Schema(
      type = "string",
      description = "변경 전 기존 비밀번호",
      example = "HelloWorld123#@"
  )
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$")
  private String oldPassword;

  @Schema(
      type = "string",
      description = "변경할 비밀번호",
      example = "Abcd1234@!"
  )
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$")
  private String newPassword;
}
