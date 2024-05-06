package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRefreshJwtRequest {

    @Schema(
        type = "string",
        example = """
            eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
            .eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
            .SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c""",
        description = "jwt 재발급을 위한 refresh token"
    )
    @NotBlank(message = "refresh token은 반드시 입력되어야 합니다.")
    private String refreshToken;
}
