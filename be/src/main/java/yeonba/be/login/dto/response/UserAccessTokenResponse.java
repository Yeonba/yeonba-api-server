package yeonba.be.login.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccessTokenResponse {

    @Schema(
        type = "string",
        example = "header.payload.signature",
        description = "새로 발급된 access token")
    private String accessToken;
}
