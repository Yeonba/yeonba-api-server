package yeonba.be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.user.dto.request.UserDormantRequest;
import yeonba.be.user.dto.request.UserJoinRequest;
import yeonba.be.user.dto.request.UserRefreshTokenRequest;
import yeonba.be.user.dto.request.UserReportRequest;
import yeonba.be.user.dto.response.BlockedUserResponse;
import yeonba.be.user.dto.response.BlockedUsersResponse;
import yeonba.be.user.dto.response.UnwantedAcquaintanceResponse;
import yeonba.be.user.dto.response.UnwantedAcquaintancesResponse;
import yeonba.be.user.dto.response.UserArrowsResponse;
import yeonba.be.user.dto.response.UserJoinResponse;
import yeonba.be.user.dto.response.UserProfileResponse;
import yeonba.be.user.dto.response.UserRefreshTokenResponse;
import yeonba.be.util.CustomResponse;

@Tag(name = "User", description = "사용자 API")
@RestController
public class UserController {

  @GetMapping("/users")
  public ResponseEntity<CustomResponse<Void>> test() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }

  @Operation(summary = "회원가입", description = "회원가입을 할 수 있습니다.")
  @PostMapping("/users/join")
  public ResponseEntity<CustomResponse<UserJoinResponse>> join(
      @RequestBody UserJoinRequest request) {

    // TODO: 회원가입 로직 구현
    String createdJwt = "created";

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(new UserJoinResponse(createdJwt)));
  }

  @Operation(summary = "만나고 싶지 않은 지인 목록 조회", description = "만나고 싶지 않은 지인 목록을 조회합니다.")
  @GetMapping("/users/unwanted-acquaintances")
  public ResponseEntity<CustomResponse<UnwantedAcquaintancesResponse>> getUnwantedAcquaintances() {

    List<UnwantedAcquaintanceResponse> sampleUnwantedAcquaintances = Arrays.asList(
        new UnwantedAcquaintanceResponse("01012345678", "안민재"),
        new UnwantedAcquaintanceResponse("01087654321", "김민재")
    );

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(new UnwantedAcquaintancesResponse(sampleUnwantedAcquaintances)));
  }

  @Operation(summary = "차단 목록 조회", description = "차단한 사용자 목록을 조회합니다.")
  @GetMapping("/users/block")
  public ResponseEntity<CustomResponse<BlockedUsersResponse>> getBlockedUsers() {

    List<BlockedUserResponse> sampleResponse = Arrays.asList(
        new BlockedUserResponse(1, "https://avatars.githubusercontent.com/u/101340860?v=4", "안민재"),
        new BlockedUserResponse(2, "https://avatars.githubusercontent.com/u/101340860?v=4", "안민재")
    );

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(new BlockedUsersResponse(sampleResponse)));
  }

  @Operation(summary = "차단 해제", description = "차단한 사용자를 해제할 수 있습니다.")
  @DeleteMapping("/users/{userId}/block")
  public ResponseEntity<CustomResponse<Void>> unblockUser(
      @Parameter(description = "사용자 ID", example = "1")
      @PathVariable long userId) {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }

  @Operation(summary = "휴면 계정 전환", description = "계정의 휴면 상태를 전환할 수 있습니다.")
  @PatchMapping("/users/dormant")
  public ResponseEntity<CustomResponse<Void>> dormantUser(
      @RequestBody UserDormantRequest request) {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }

  @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 할 수 있습니다.")
  @DeleteMapping("/users")
  public ResponseEntity<CustomResponse<Void>> deleteUser() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
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

  @Operation(
      summary = "사용자 화살 개수 조회",
      description = "사용자 화살 개수를 조회할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "사용자 화살 개수 정상 조회"
  )
  @GetMapping("/users/arrows")
  public ResponseEntity<CustomResponse<UserArrowsResponse>> arrows() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(new UserArrowsResponse(10)));
  }

  @Operation(
      summary = "출석 체크",
      description = "출석 체크를 통해 사용자가 10개의 화살을 획득할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "정상적으로 출석 체크 완료"
  )
  @PostMapping("/daily-check")
  public ResponseEntity<CustomResponse<Void>> dailyCheck() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "사용자 프로필 조회",
      description = "다른 사용자의 프로필을 조회할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "사용자 프로필 정상 조회"
  )
  @GetMapping("/users/{userId}")
  public ResponseEntity<CustomResponse<UserProfileResponse>> profile(
      @Parameter(description = "조회대상 사용자 ID", example = "1")
      @PathVariable long userId) {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(
            new UserProfileResponse(
                "존잘남",
                23,
                177,
                "서울시 강남구",
                80,
                "저음",
                "여우상",
                false
            )
        ));
  }

  @Operation(
      summary = "사용자 신고",
      description = "다른 사용자를 신고할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "204",
      description = "신고 정상 처리"
  )
  @PostMapping("/users/{userId}/report")
  public ResponseEntity<CustomResponse<Void>> report(
      @Parameter(description = "신고 대상 사용자 ID", example = "1")
      @PathVariable long userId,
      @RequestBody UserReportRequest request) {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }
}
