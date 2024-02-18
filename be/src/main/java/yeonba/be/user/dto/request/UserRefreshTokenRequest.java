package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRefreshTokenRequest {
	@Schema(
		type = "string",
		example = """
			eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
			.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
			.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c""",
		description = "access token 재발급을 위한 refresh token"
	)
	private String refreshToken;
}
