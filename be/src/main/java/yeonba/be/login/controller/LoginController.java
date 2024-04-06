package yeonba.be.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.LoginException;
import yeonba.be.login.dto.request.UserEmailInquiryRequest;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.login.dto.request.UserLoginRequest;
import yeonba.be.login.dto.request.UserPasswordInquiryRequest;
import yeonba.be.login.dto.request.UserVerificationCodeRequest;
import yeonba.be.login.dto.request.UserVerifyPhoneNumberRequest;
import yeonba.be.login.dto.response.UserAccessTokenResponse;
import yeonba.be.login.dto.response.UserEmailInquiryResponse;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.login.dto.response.UserLoginResponse;
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
    public ResponseEntity<CustomResponse<UserAccessTokenResponse>> join(
        @Valid @ModelAttribute UserJoinRequest request,
        HttpServletResponse response) {

        UserJoinResponse joinResponse = joinService.join(request);

        setRefreshTokenCookie(response, joinResponse.getRefreshToken());
        UserAccessTokenResponse accessTokenResponse =
            new UserAccessTokenResponse(joinResponse.getAccessToken());

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(accessTokenResponse));
    }

    @Operation(summary = "이메일 찾기 인증 코드 sms 전송", description = "이메일 찾기를 위한 인증번호 sms 전송을 요청합니다.")
    @ApiResponse(responseCode = "202", description = "전화번호 인증 코드 전송 성공")
    @PostMapping("/users/email-inquiry/verification-code")
    public ResponseEntity<CustomResponse<Void>> verifyPhoneNumber(
        @Valid @RequestBody UserVerificationCodeRequest request) {

        loginService.sendVerificationCodeMessage(request);

        return ResponseEntity
            .accepted()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "이메일 찾기", description = "인증 코드를 바탕으로 아이디를 찾을 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "아이디 찾기 정상 처리")
    @PostMapping("/users/email-inquiry")
    public ResponseEntity<CustomResponse<UserEmailInquiryResponse>> emailInquiry(
        @Valid @RequestBody UserEmailInquiryRequest request) {

        UserEmailInquiryResponse response = loginService.findEmail(request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "비밀번호 찾기", description = "이메일로 임시 비밀번호를 발급받을 수 있습니다.")
    @ApiResponse(responseCode = "202", description = "임시 비밀번호 발급(비밀번호 찾기) 정상 처리")
    @PostMapping("/users/pw-inquiry")
    public ResponseEntity<CustomResponse<Void>> passwordInquiry(
        @Valid @RequestBody UserPasswordInquiryRequest request) {

        loginService.sendTemporaryPasswordMail(request);

        return ResponseEntity
            .accepted()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "로그인", description = "로그인을 할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @PostMapping("/users/login")
    public ResponseEntity<CustomResponse<UserAccessTokenResponse>> login(
        @Valid @RequestBody UserLoginRequest request,
        HttpServletResponse response) {

        UserLoginResponse loginResponse = loginService.login(request);

        // refresh token을 전달할 cookie 설정
        setRefreshTokenCookie(response, loginResponse.getRefreshToken());

        UserAccessTokenResponse accessTokenResponse =
            new UserAccessTokenResponse(loginResponse.getAccessToken());

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(accessTokenResponse));
    }

    private void setRefreshTokenCookie(
        HttpServletResponse response,
        String refreshToken) {

        ResponseCookie refreshTokenCookie = ResponseCookie
            .from("refreshToken", refreshToken)
            .path("/users/refresh")
            .secure(true)
            .sameSite("Strict")
            .httpOnly(true)
            .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    @Operation(summary = "access token 재발급", description = "refresh token 통해 access token 재발급")
    @ApiResponse(responseCode = "200", description = "access token 재발급 성공")
    @PostMapping("/users/refresh")
    public ResponseEntity<CustomResponse<UserAccessTokenResponse>> refresh(
        HttpServletRequest request) {

        // refresh token cookie 탐색 및 검증
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals("refreshToken"))
            .findFirst();
        if (refreshTokenCookie.isEmpty()) {
            throw new GeneralException(LoginException.REFRESH_TOKEN_NOT_EXIST);
        }

        String refreshToken = refreshTokenCookie.get().getValue();
        UserAccessTokenResponse response = loginService.refreshAccessToken(refreshToken);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "핸드폰 번호 인증 코드 sms 전송", description = "핸드 번호 인증 코드 sms 전송")
    @ApiResponse(responseCode = "202", description = "인증 코드 전송 정상 처리")
    @PostMapping("/users/join/phone-number/verification-code")
    public ResponseEntity<CustomResponse<Void>> verifyJoinPhoneNumber(
        @Valid @RequestBody UserVerificationCodeRequest request) {

        loginService.sendJoinVerificationCodeMessage(request);

        return ResponseEntity
            .accepted()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "핸드폰 번호 인증", description = "회원가입 과정서 핸드폰 번호 인증")
    @ApiResponse(responseCode = "202", description = "핸드폰 번호 인증 정상 처리")
    @PostMapping("/users/join/phone-number")
    public ResponseEntity<CustomResponse<Void>> verifyPhoneNumber(
        @Valid @RequestBody UserVerifyPhoneNumberRequest request) {

        loginService.verifyPhoneNumber(request);

        return ResponseEntity
            .accepted()
            .body(new CustomResponse<>());
    }
}
