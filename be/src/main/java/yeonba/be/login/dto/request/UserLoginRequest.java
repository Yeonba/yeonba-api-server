package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

	@Schema(
		type = "string",
		description = "이메일",
		example = "mj3242@naver.com")
	@Pattern(
		regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
		message = "유효하지 않은 이메일 형식입니다.")
	@NotBlank(message = "이메일은 반드시 입력되어야 합니다.")
	private String email;

	@Schema(
		type = "string",
		description = "비밀번호",
		example = "examplePassword!")
	@Pattern(
		regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$",
		message = """
			비밀번호는 영어대소문자, 숫자, 특수문자(~#@!)를
			최소 1자씩 포함하며 8~20자 사이여야 합니다.""")
	@NotBlank(message = "비밀번호는 반드시 입력되어야 합니다.")
	private String password;
}
