package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    @Schema(
        type = "number",
        description = "소셜 ID",
        example = "3838743948"
    )
    @NotNull(message = "소셜 ID는 반드시 입력되어야 합니다.")
    private long socialId;

    @Schema(
        type = "string",
        description = "소셜 로그인 타입",
        example = "KAKAO"
    )
    @NotBlank(message = "소셜 로그인 타입은 반드시 입력되어야 합니다.")
    private String loginType;

    @Schema(
        type = "string",
        description = "전화 번호",
        example = "01012345678"
    )
    private String phoneNumber;
}
