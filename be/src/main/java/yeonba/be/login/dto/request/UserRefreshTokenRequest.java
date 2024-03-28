package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRefreshTokenRequest {

    @Schema(
        type = "string",
        example = "header.payload.signature",
        description = "access token 재발급을 위한 refresh token")
    @NotBlank(message = "refresh token은 반드시 입력되어야 합니다.")
    private String refreshToken;
}
