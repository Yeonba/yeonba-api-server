package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordInquiryRequest {

  @Schema(
      type = "string",
      description = "임시 비밀번호를 받을 이메일",
      example = "mj3242@naver.com"
  )
  private String email;
}
