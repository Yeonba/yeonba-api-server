package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEmailInquiryRequest {

	@Schema(
		type = "string",
		description = "인증 번호를 받은 번호",
		example = "01011112222")
	@Pattern(
		regexp = "^010\\d{8}$",
		message = "전화번호는 11자리 010으로 시작하며 하이픈(-) 없이 0~9의 숫자로 이뤄져야 합니다.")
	private String phoneNumber;

	@Schema(
		type = "string",
		description = "아이디 찾기 인증 코드",
		example = "A1b2C3")
	@Pattern(
		regexp = "^[A-Za-z0-9]{6}$",
		message = "인증 코드는 6자리로 영어대소문자, 숫자로만 이뤄져야 합니다.")
	private String verificationCode;
}
