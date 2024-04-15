package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordInquiryRequest {

	@Schema(type = "string",
		description = "임시 비밀번호를 받을 이메일",
		example = "mj3242@naver.com")
	@Pattern(
		regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
		message = "유효하지 않은 이메일 형식입니다.")
	private String email;
}
