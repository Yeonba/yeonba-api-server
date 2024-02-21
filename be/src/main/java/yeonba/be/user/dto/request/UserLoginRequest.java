package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

  @Schema(
      type = "string",
      description = "이메일",
      example = "mj3242@naver.com"
  )
  private String email;

  @Schema(
      type = "string",
      description = "비밀번호",
      example = "examplePassword!"
  )
  private String password;
}
