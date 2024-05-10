package yeonba.be.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yeonba.be.mypage.dto.request.UserAllowNotificationsRequest;
import yeonba.be.mypage.dto.request.UserDormantRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.request.UserUpdateUnwantedAcquaintancesRequest;
import yeonba.be.mypage.dto.response.BlockedUsersResponse;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.mypage.service.AcquaintanceService;
import yeonba.be.mypage.service.MyPageService;
import yeonba.be.notification.dto.response.NotificationPermissionsResponse;
import yeonba.be.util.CustomResponse;

@Tag(name = "MyPage", description = " My Page 관련 API")
@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final AcquaintanceService acquaintanceService;

    @Operation(summary = "자신의 프로필 조회", description = "사용자 자신의 프로필 정보를 조회할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "자신의 프로필 조회 성공")
    @GetMapping("/users/profiles")
    public ResponseEntity<CustomResponse<UserSimpleProfileResponse>> getSimpleProfile(
        @RequestAttribute("userId") long userId) {

        UserSimpleProfileResponse response = myPageService.getSimpleProfile(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "자신의 상세 프로필 조회", description = "사용자 자신의 상세 프로필 정보를 조회할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "자신의 상세 프로필 조회 성공")
    @GetMapping("/users/profiles/details")
    public ResponseEntity<CustomResponse<UserProfileDetailResponse>> getProfileDetail(
        @RequestAttribute("userId") long userId) {

        UserProfileDetailResponse response = myPageService.getProfileDetail(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "자신의 프로필 사진 수정", description = "자신의 프로필 사진을 수정할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "자신의 프로필 사진 수정 정상 처리")
    @PutMapping(path = "/users/profile-photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse<Void>> updateProfilePhotos(
        @RequestAttribute("userId") long userId,
        @Parameter(description = "업로드 할 새로운 프로필 사진들, 반드시 한 번에 2개씩 업로드")
        @RequestPart("profilePhotos") @Size(min = 2, max = 2) List<MultipartFile> profilePhotos,
        @Parameter(description = "프로필 사진과 싱크로율을 검증할 직접 찍은 사진")
        @RequestPart("realTimePhoto") @Size(min = 1, max = 1) MultipartFile realTimePhoto) {

        myPageService.updateProfilePhotos(profilePhotos, realTimePhoto, userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "자신의 프로필 수정", description = "자신의 프로필 정보를 수정할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "자신의 프로필 수정 요청 정상 처리")
    @PatchMapping("/users/profiles")
    public ResponseEntity<CustomResponse<Void>> updateProfile(
        @Valid @RequestBody UserUpdateProfileRequest request,
        @RequestAttribute("userId") long userId) {

        myPageService.updateProfile(request, userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "알림 동의 내역(on/off) 조회", description = "알림 동의 내역을 조회할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "알림 동의 내역 조회 성공")
    @GetMapping("/users/notifications/permissions")
    public ResponseEntity<CustomResponse<NotificationPermissionsResponse>>
    getNotificationPermissions(@RequestAttribute("userId") long userId) {

        NotificationPermissionsResponse response =
            myPageService.getNotificationPermissions(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "알림 on/off 설정", description = "알림별로 on/off를 설정할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "알림 on/off 설정 정상 처리")
    @PatchMapping("/users/notifications/permissions")
    public ResponseEntity<CustomResponse<Void>> allowNotifications(
        @RequestAttribute("userId") long userId,
        @Valid @RequestBody UserAllowNotificationsRequest request) {

        myPageService.updateNotificationPermissions(userId, request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "만나고 싶지 않은 지인 목록 수정", description = "만나고 싶지 않은 지인을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "지인 목록 추가 정상 처리")
    @PutMapping("/users/unwanted-acquaintances")
    public ResponseEntity<CustomResponse<Void>> updateUnwantedAcquaintances(
        @RequestAttribute("userId") long userId,
        @RequestBody UserUpdateUnwantedAcquaintancesRequest request) {

        acquaintanceService.updateUnwantedAcquaintances(userId, request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "차단 목록 조회", description = "차단한 사용자 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "차단 목록 조회 성공")
    @GetMapping("/users/block")
    public ResponseEntity<CustomResponse<BlockedUsersResponse>> getBlockedUsers(
        @RequestAttribute("userId") long userId) {

        BlockedUsersResponse response = myPageService.getBlockedUsers(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "차단 해제", description = "차단한 사용자를 해제할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "차단 해제 정상 처리")
    @DeleteMapping("/users/{userId}/block")
    public ResponseEntity<CustomResponse<Void>> unblockUser(
        @RequestAttribute("userId") long userId,
        @Parameter(description = "사용자 ID", example = "1") @PathVariable("userId") long blockedUserId) {

        myPageService.unblockUser(userId, blockedUserId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "휴면 계정 전환", description = "계정의 휴면 상태를 전환할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "휴면 상태 전환 요청 정상 처리")
    @PatchMapping("/users/dormant")
    public ResponseEntity<CustomResponse<Void>> dormantUser(
        @RequestAttribute("userId") long userId,
        @Valid @RequestBody UserDormantRequest request) {

        myPageService.changeDormantStatus(userId, request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 할 수 있습니다. 즉시 탈퇴 처리됩니다.")
    @ApiResponse(responseCode = "200", description = "계정 탈퇴 요청 정상 처리")
    @DeleteMapping("/users")
    public ResponseEntity<CustomResponse<Void>> deleteUser(
        @RequestAttribute("userId") long userId) {

        myPageService.deleteUser(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }
}
