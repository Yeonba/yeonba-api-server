package yeonba.be.mypage.service;

import static yeonba.be.notification.entity.NotificationType.ARROW_RECEIVED;
import static yeonba.be.notification.entity.NotificationType.CHATTING_REQUESTED;
import static yeonba.be.notification.entity.NotificationType.CHATTING_REQUEST_ACCEPTED;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.NotificationException;
import yeonba.be.mypage.dto.request.UserAllowNotificationsRequest;
import yeonba.be.mypage.dto.request.UserDormantRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.BlockedUserResponse;
import yeonba.be.mypage.dto.response.BlockedUsersResponse;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.notification.dto.response.NotificationPermissionsResponse;
import yeonba.be.notification.entity.NotificationPermission;
import yeonba.be.notification.entity.NotificationType;
import yeonba.be.notification.repository.NotificationPermissionCommand;
import yeonba.be.notification.repository.NotificationPermissionQuery;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.BlockCommand;
import yeonba.be.user.repository.BlockQuery;
import yeonba.be.user.repository.user.UserQuery;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final S3Client s3Client;

    private final BlockQuery blockQuery;
    private final UserQuery userQuery;
    private final NotificationPermissionQuery notificationPermissionQuery;
    private final NotificationPermissionCommand notificationPermissionCommand;

    private final BlockCommand blockCommand;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    @Transactional(readOnly = true)
    public UserSimpleProfileResponse getSimpleProfile(long userId) {

        User user = userQuery.findById(userId);

        return new UserSimpleProfileResponse(
            user.getNickname(),
            user.getRepresentativeProfilePhoto(),
            user.getArrow()
        );
    }

    @Transactional(readOnly = true)
    public UserProfileDetailResponse getProfileDetail(long userId) {

        User user = userQuery.findById(userId);

        return new UserProfileDetailResponse(user);
    }

    @Transactional
    public void updateProfile(UserUpdateProfileRequest request, long userId) {

        User validatedUser = userQuery.findById(userId);

        // TODO: 선호 조건 테이블 생성 후 로직 추가

        // validatedUser.updateProfile(request);
    }

    public void updateProfilePhotos(List<MultipartFile> profilePhotos, MultipartFile realTimePhoto,
        long userId) {

        User user = userQuery.findById(userId);

        // TODO: AI server 연동 후 얼굴 인식 로직 추가
        // TODO: 사용자마다 정해전 경로에 파일을 업로드 하기 때문에 회원 가입 시 파일을 저장할 경로를 만들어야 함.
        uploadProfilePhotos(profilePhotos, user);
    }

    public BlockedUsersResponse getBlockedUsers(long userId) {

        User user = userQuery.findById(userId);

        List<Block> blocks = blockQuery.findBlocksByUser(user);

        List<BlockedUserResponse> blockedUsers = blocks.stream()
            .map(block -> new BlockedUserResponse(
                block.getBlockedUser())
            )
            .toList();

        return new BlockedUsersResponse(blockedUsers);
    }

    @Transactional
    public void unblockUser(long userId, long blockedUserId) {

        User user = userQuery.findById(userId);
        User blockedUser = userQuery.findById(blockedUserId);

        Block block = blockQuery.findByUsers(user, blockedUser);
        blockCommand.delete(block);
    }

    @Transactional
    public void changeDormantStatus(long userId, UserDormantRequest request) {

        User user = userQuery.findById(userId);
        user.changeInactiveStatus(request.isStatus());
    }

    @Transactional
    public void deleteUser(long userId) {

        User user = userQuery.findById(userId);
        user.delete();
    }

    /**
     * 사용자마다 정해진 profile photo url에 파일을 업로드한다.
     */
    private void uploadProfilePhotos(List<MultipartFile> profilePhotos, User user) {

        List<String> fileNames = user.getProfilePhotoUrls();

        // TODO: 회의 후 확장자 제한 로직 추가, 확장자 검증 후 업로드 시작
        // validateFileExtension(profilePhoto);

        for (int profilePhotoIdx = 0; profilePhotoIdx < profilePhotos.size();
            profilePhotoIdx++) {

            MultipartFile profilePhoto = profilePhotos.get(profilePhotoIdx);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileNames.get(profilePhotoIdx))
                .contentDisposition("inline")
                .contentType(profilePhoto.getContentType())
                .build();

            try {
                s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(profilePhoto.getInputStream(),
                        profilePhoto.getSize()));

            } catch (Exception e) {
                throw new IllegalStateException(
                    "Failed to upload file: " + profilePhoto.getOriginalFilename(), e);
            }
        }
    }

    @Transactional(readOnly = true)
    public NotificationPermissionsResponse getNotificationPermissions(long userId) {

        User user = userQuery.findById(userId);
        List<NotificationPermission> notificationPermissions =
            notificationPermissionQuery.findAllByUser(user);

        boolean arrowReceivedNotificationPermission =
            getNotificationPermissionStatusBy(notificationPermissions, ARROW_RECEIVED);
        boolean chattingRequestNotificationPermission =
            getNotificationPermissionStatusBy(notificationPermissions, CHATTING_REQUESTED);
        boolean chattingRequestAcceptedNotificationPermission =
            getNotificationPermissionStatusBy(notificationPermissions, CHATTING_REQUEST_ACCEPTED);

        return new NotificationPermissionsResponse(
            arrowReceivedNotificationPermission,
            chattingRequestNotificationPermission,
            chattingRequestAcceptedNotificationPermission);
    }

    private boolean getNotificationPermissionStatusBy(
        List<NotificationPermission> notificationPermissions, NotificationType type) {

        Optional<NotificationPermission> foundPermission = notificationPermissions.stream()
            .filter(notificationPermission -> notificationPermission.hasSameTypeAs(type))
            .findFirst();

        return foundPermission.map(NotificationPermission::getPermissionStatus)
            .orElseThrow(() ->
                new GeneralException(NotificationException.NOTIFICATION_PERMISSION_NOT_FOUND));
    }

    @Transactional
    public void updateNotificationPermissions(long userId, UserAllowNotificationsRequest request) {

        // 사용자 및 사용자 동의 내역 목록 조회
        User user = userQuery.findById(userId);
        List<NotificationPermission> notificationPermissions =
            notificationPermissionQuery.findAllByUser(user);

        // 알림 타입, 알림 동의 내역 Map 구성
        Map<NotificationType, NotificationPermission> typePermissionMap =
            notificationPermissions.stream()
                .collect(Collectors.toMap(NotificationPermission::getType, Function.identity()));

        // 알림 타입, 알림 동의 상태 Map 구성
        Map<NotificationType, Boolean> typePermissonStatusMap = new HashMap<>();
        typePermissonStatusMap.put(ARROW_RECEIVED, request.isAllowArrowReceivedNotification());
        typePermissonStatusMap.put(CHATTING_REQUESTED,
            request.isAllowChattingRequestNotification());
        typePermissonStatusMap.put(
            CHATTING_REQUEST_ACCEPTED, request.isAllowChattingRequestAcceptedNotification());

        // 알림 타입별 내역 수정 or 내역 생성 작업 수행
        typePermissonStatusMap.forEach((type, permissionStatus) ->
            updateOrCreateNotificationPermissionBy(typePermissionMap, user, type, permissionStatus)
        );
    }

    private void updateOrCreateNotificationPermissionBy(
        Map<NotificationType, NotificationPermission> typePermissonMap,
        User user,
        NotificationType type,
        boolean permissionStatus) {

        Optional<NotificationPermission> foundNotificationPermission =
            Optional.ofNullable(typePermissonMap.get(type));

        // 동의 내역이 존재할 경우 동의 상태 업데이트
        if (foundNotificationPermission.isPresent()) {
            foundNotificationPermission.get().updatePermissionStatus(permissionStatus);

            return;
        }

        // 동의 내역이 존재하지 않을 시, 생성 후 저장
        NotificationPermission notificationPermission =
            new NotificationPermission(permissionStatus, type, user);
        notificationPermissionCommand.save(notificationPermission);
    }
}
