package yeonba.be.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserIdInquiryRequest {

  @Schema(
      type = "string",
      description = "아이디 찾기 인증 코드",
      example = "CYQR-XIUH-DXMA-LZVN"
  )
  private String verificationCode;
}
