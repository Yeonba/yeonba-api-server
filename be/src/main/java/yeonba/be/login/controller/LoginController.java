package yeonba.be.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.login.dto.request.UserLoginRequest;
import yeonba.be.login.dto.request.UserRefreshJwtRequest;
import yeonba.be.login.dto.request.UserValidateUsedNicknameRequest;
import yeonba.be.login.dto.request.UserValidateUsedPhoneNumberRequest;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.login.dto.response.UserLoginResponse;
import yeonba.be.login.dto.response.UserRefrehJwtResponse;
import yeonba.be.login.dto.response.UserValidateUsedNicknameResponse;
import yeonba.be.login.dto.response.UserValidateUsedPhoneNumberResponse;
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

    @Operation(summary = "사용 중인 닉네임 검증", description = "사용 중인 닉네임인 지 검증할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "사용 중인 닉네임 검증 성공")
    @GetMapping("/users/nicknames/used")
    public ResponseEntity<CustomResponse<UserValidateUsedNicknameResponse>> validateUsedNickname(
        @Valid @ParameterObject UserValidateUsedNicknameRequest request) {

        UserValidateUsedNicknameResponse response =
            loginService.validateUsedNickname(request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "사용 중인 전화번호 검증", description = "사용 중인 전화번호 검증 가능")
    @ApiResponse(responseCode = "200", description = "사용 중인 전화번호 검증 성공")
    @GetMapping("/users/phone-numbers/used")
    public ResponseEntity<CustomResponse<UserValidateUsedPhoneNumberResponse>>
    validateUsedPhoneNumber(@Valid @ParameterObject UserValidateUsedPhoneNumberRequest request) {

        UserValidateUsedPhoneNumberResponse response =
            loginService.validateUsedPhoneNumber(request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }
}
