package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private long socialId;

    @Schema(
        type = "string",
        description = "소셜 로그인 타입",
        example = "KAKAO"
    )
    private String loginType;

    @Schema(
        type = "string",
        description = "전화 번호",
        example = "01012345678"
    )
    private String phoneNumber;
}
