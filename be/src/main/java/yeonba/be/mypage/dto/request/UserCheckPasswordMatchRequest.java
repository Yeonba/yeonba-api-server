package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCheckPasswordMatchRequest {

  @Schema(
      type = "string",
      description = "현재 비밀번호 값, 이 값을 DB에 저장된 값과 비교하여 검증",
      example = "HelloWorld123#@"
  )
  @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$")
  private String password;
}
