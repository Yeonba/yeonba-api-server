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
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.login.dto.request.UserLoginRequest;
import yeonba.be.login.dto.request.UserRefreshJwtRequest;
import yeonba.be.login.dto.request.UserVerificationCodeRequest;
import yeonba.be.login.dto.request.UserVerifyPhoneNumberRequest;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.login.dto.response.UserLoginResponse;
import yeonba.be.login.dto.response.UserRefrehJwtResponse;
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
    @ApiResponse(responseCode = "200", description = "회원가입 성공")
    @PostMapping(path = "/users/join", consumes = "multipart/form-data")
    public ResponseEntity<CustomResponse<UserJoinResponse>> join(
        @Valid @ModelAttribute UserJoinRequest request) {

        UserJoinResponse response = joinService.join(request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @PostMapping("/users/login")
    public ResponseEntity<CustomResponse<UserLoginResponse>> login(
        @Valid @RequestBody UserLoginRequest request) {

        UserLoginResponse response = loginService.login(request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "jwt 재발급", description = "refresh token을 통해 jwt를 재발급받을 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "jwt 재발급 성공")
    @PostMapping("/users/refresh")
    public ResponseEntity<CustomResponse<UserRefrehJwtResponse>> refreshJwt(
        @RequestBody UserRefreshJwtRequest request) {

        UserRefrehJwtResponse response = loginService.refreshJwt(request);

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
