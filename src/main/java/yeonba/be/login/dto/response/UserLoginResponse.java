package yeonba.be.login.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {

  @Schema(
      type = "string",
      description = "access token",
      example = """
          eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
          .eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
          .SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"""
  )
  private String accessToken;

  @Schema(
      type = "string",
      description = "refresh token",
      example = """
          eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
          .eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
          .SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"""
  )
  private String refreshToken;

}
