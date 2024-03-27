package yeonba.be.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.login.dto.request.UserIdInquiryRequest;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.login.dto.request.UserLoginRequest;
import yeonba.be.login.dto.request.UserPasswordInquiryRequest;
import yeonba.be.login.dto.request.UserPhoneNumberVerifyRequest;
import yeonba.be.login.dto.request.UserRefreshTokenRequest;
import yeonba.be.login.dto.response.UserIdInquiryResponse;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.login.dto.response.UserLoginResponse;
import yeonba.be.login.dto.response.UserRefreshTokenResponse;
import yeonba.be.login.service.LoginService;
import yeonba.be.user.service.JoinService;
import yeonba.be.util.CustomResponse;

@Tag(name = "Login", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;
	private final JoinService joinService;

	@Operation(summary = "회원가입", description = "회원가입을 할 수 있습니다.")
	@PostMapping(path = "/users/join", consumes = "multipart/form-data")
	public ResponseEntity<CustomResponse<UserJoinResponse>> join(
		@Valid @ModelAttribute UserJoinRequest request) {

		UserJoinResponse response = joinService.join(request);

		return ResponseEntity
			.ok()
			.body(new CustomResponse<>(response));
	}

	@Operation(
		summary = "전화번호 인증 코드 전송",
		description = "전화번호 인증을 위해 해당 번호로 인증 코드를 발송합니다."
	)
	@ApiResponse(
		responseCode = "204",
		description = "전화번호 인증 코드 전송 성공"
	)
	@PostMapping("/users/help/id-inquiry/verification-code")
	public ResponseEntity<CustomResponse<Void>> verifyPhoneNumber(
		@RequestBody UserPhoneNumberVerifyRequest request) {

		return ResponseEntity
			.accepted()
			.body(new CustomResponse<>());
	}

	@Operation(
		summary = "아이디 찾기",
		description = "인증 코드를 바탕으로 아이디를 찾을 수 있습니다."
	)
	@ApiResponse(
		responseCode = "200",
		description = "아이디 찾기 정상 처리"
	)
	@PostMapping("/users/help/id-inquiry")
	public ResponseEntity<CustomResponse<UserIdInquiryResponse>> idInquiry(
		@RequestBody UserIdInquiryRequest request) {

		return ResponseEntity
			.ok()
			.body(new CustomResponse<>(new UserIdInquiryResponse("mj3242@naver.com")));
	}

	@Operation(summary = "비밀번호 찾기", description = "이메일로 임시 비밀번호를 발급받을 수 있습니다.")
	@ApiResponse(responseCode = "202", description = "임시 비밀번호 발급(비밀번호 찾기) 정상 처리")
	@PostMapping("/users/help/pw-inquiry")
	public ResponseEntity<CustomResponse<Void>> passwordInquiry(
		@Valid @RequestBody UserPasswordInquiryRequest request) {

		loginService.sendTemporaryPasswordMail(request);

		return ResponseEntity
			.accepted()
			.body(new CustomResponse<>());
	}

	@Operation(summary = "로그인", description = "로그인을 할 수 있습니다.")
	@PostMapping("/users/login")
	public ResponseEntity<CustomResponse<UserLoginResponse>> login(
		@RequestBody UserLoginRequest request) {

		return ResponseEntity
			.ok()
			.body(new CustomResponse<>(
				new UserLoginResponse(
					"""
						eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
						.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
						.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c""",
					"""
						eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
						.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
						.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"""
				)));
	}

	@Operation(
		summary = "access token 재발급",
		description = "refresh token을 통해 access token을 재발급받을 수 있습니다."
	)
	@PostMapping("/users/refresh")
	public ResponseEntity<CustomResponse<UserRefreshTokenResponse>> refresh(
		@RequestBody UserRefreshTokenRequest request) {

		String createdJwt = "created";

		return ResponseEntity
			.ok()
			.body(new CustomResponse<>(new UserRefreshTokenResponse(createdJwt)));
	}
}
