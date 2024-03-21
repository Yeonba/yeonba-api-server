package yeonba.be.login.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

	@Schema(
		type = "string",
		description = "access token",
		example = "header.payload,signature")
	private String accessToken;

	@Schema(
		type = "string",
		description = "refresh token",
		example = "header.payload.signature")
	private String refreshToken;
}
