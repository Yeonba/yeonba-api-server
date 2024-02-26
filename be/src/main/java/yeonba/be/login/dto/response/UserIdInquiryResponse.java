package yeonba.be.login.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserIdInquiryResponse {

  @Schema(
      type = "string",
      description = "이메일",
      example = "mj3242@naver.com"
  )
  private String email;
}
