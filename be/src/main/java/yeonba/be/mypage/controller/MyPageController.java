package yeonba.be.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import yeonba.be.mypage.dto.request.UserAllowNotificationsRequest;
import yeonba.be.mypage.dto.request.UserChangePasswordRequest;
import yeonba.be.mypage.dto.request.UserCheckPasswordMatchRequest;
import yeonba.be.mypage.dto.request.UserDormantRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.BlockedUserResponse;
import yeonba.be.mypage.dto.response.BlockedUsersResponse;
import yeonba.be.mypage.dto.response.UnwantedAcquaintanceResponse;
import yeonba.be.mypage.dto.response.UnwantedAcquaintancesResponse;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserProfileImageResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.util.CustomResponse;

@Tag(name = "MyPage", description = " My Page 관련 API")
@RestController
public class MyPageController {

  @Operation(
      summary = "자신의 프로필 조회",
      description = "사용자 자신의 프로필 정보를 조회할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "자신의 프로필 조회 성공"
  )
  @GetMapping("/users/profile")
  public ResponseEntity<CustomResponse<UserSimpleProfileResponse>> simpleProfile() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(new UserSimpleProfileResponse(
            "안민재",
            "https://yeonba-bucket.s3.ap-northeast-2.amazonaws.com/wanna_go_home.jpg",
            10
        )));
  }

  @Operation(
      summary = "자신의 상세 프로필 조회",
      description = "사용자 자신의 상세 프로필 정보를 조회할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "자신의 상세 프로필 조회 성공"
  )
  @GetMapping("/users/profile/detail")
  public ResponseEntity<CustomResponse<UserProfileDetailResponse>> profileDetail() {
    
    List<UserProfileImageResponse> profileImages = List.of(
        new UserProfileImageResponse(
            1L,
            "https://yeonba-bucket.s3.ap-northeast-2.amazonaws.com/wanna_go_home.jpg",
            LocalDateTime.of(2012, 10, 1, 12, 0, 5)
        ),
        new UserProfileImageResponse(
            2L,
            "https://yeonba-bucket.s3.ap-northeast-2.amazonaws.com/wanna_go_home.jpg",
            LocalDateTime.of(2012, 10, 1, 12, 0, 5)
        ),
        new UserProfileImageResponse(
            3L,
            "https://yeonba-bucket.s3.ap-northeast-2.amazonaws.com/wanna_go_home.jpg",
            LocalDateTime.of(2012, 10, 1, 12, 0, 5)
        )
    );

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(new UserProfileDetailResponse(
            profileImages,
            "남",
            "안민재",
            LocalDate.of(1998, 1, 1),
            181,
            "mj3242@naver.com",
            "01011112222",
            "존잘남",
            80,
            "마른 체형",
            "학생",
            "자주",
            "가끔",
            "ISTP",
            LocalDateTime.of(2012, 10, 1, 11, 30, 2)
        )));
  }

  @Operation(
      summary = "현재 비밀번호 확인",
      description = "입력한 비밀번호가 현재 비밀번호와 일치하는 지 검증합니다."
  )
  @ApiResponse(
      responseCode = "202",
      description = "입력한 비밀번호 검증 완료"
  )
  @PostMapping("/users/password")
  public ResponseEntity<CustomResponse<Void>> checkPasswordMatch(
      @RequestBody UserCheckPasswordMatchRequest request) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "비밀번호 수정",
      description = "자신의 비밀번호를 수정할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "202",
      description = "비밀번호 수정 완료"
  )
  @PatchMapping("/users/password")
  public ResponseEntity<CustomResponse<Void>> changePassword(
      @RequestBody UserChangePasswordRequest request) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "자산의 프로필 수정",
      description = "자신의 프로필 정보를 수정할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "204",
      description = "자신의 프로필 수정 요청 정상 처리"
  )
  @PatchMapping("/users/profile")
  public ResponseEntity<CustomResponse<Void>> updateProfile(
      @RequestBody UserUpdateProfileRequest request
  ) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "알림 on/off 설정",
      description = "알림별로 on/off를 설정할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "204",
      description = "알림 on/off 설정 정상 처리"
  )
  @PatchMapping("/user/notifications")
  public ResponseEntity<CustomResponse<Void>> allowNotifications(
      @RequestBody UserAllowNotificationsRequest request
  ) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
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
}
