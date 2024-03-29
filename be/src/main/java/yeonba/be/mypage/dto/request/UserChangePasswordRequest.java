package yeonba.be.mypage.dto.request;

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
      description = "변경할 새 비밀번호",
      example = "Abcd1234@!"
  )
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$")
  private String newPassword;

  @Schema(
      type = "string",
      description = "변경할 새 비밀번호 확인 값, 검증에 쓰인다.",
      example = "Abcd1234@!"
  )
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$")
  private String newPasswordConfirmation;
}
