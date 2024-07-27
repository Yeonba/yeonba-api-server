package yeonba.be.mypage.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.BlockException;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.NotificationException;
import yeonba.be.exception.UserException;
import yeonba.be.mypage.dto.NotificationPermissionDetail;
import yeonba.be.mypage.dto.request.NotificationPermissionsUpdateRequest;
import yeonba.be.mypage.dto.request.UserChangeInactiveStatusRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfilePhotoRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.BlockedUserResponse;
import yeonba.be.mypage.dto.response.BlockedUsersResponse;
import yeonba.be.mypage.dto.response.NotificationPermissionsResponse;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.notification.entity.NotificationPermission;
import yeonba.be.notification.enums.NotificationType;
import yeonba.be.notification.repository.NotificationPermissionQuery;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;
import yeonba.be.user.entity.VocalRange;
import yeonba.be.user.repository.animal.AnimalQuery;
import yeonba.be.user.repository.area.AreaQuery;
import yeonba.be.user.repository.block.BlockCommand;
import yeonba.be.user.repository.block.BlockQuery;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.user.repository.userpreference.UserPreferenceQuery;
import yeonba.be.user.repository.vocalrange.VocalRangeQuery;
import yeonba.be.util.AgeValidator;
import yeonba.be.util.BoundsValidator;
import yeonba.be.util.S3Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final AnimalQuery animalQuery;
    private final AreaQuery areaQuery;
    private final BlockQuery blockQuery;
    private final UserPreferenceQuery userPreferenceQuery;
    private final UserQuery userQuery;
    private final VocalRangeQuery vocalRangeQuery;
    private final NotificationPermissionQuery notificationPermissionQuery;

    private final BlockCommand blockCommand;

    private final S3Service s3Service;

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
        UserPreference userPreference = userPreferenceQuery.findByUser(user);

        return UserProfileDetailResponse.from(user, userPreference);
    }

    @Transactional
    public void updateProfile(UserUpdateProfileRequest request, long userId) {

        // 사용자 및 사용자 선호 조건 조회
        User user = userQuery.findById(userId);
        UserPreference userPreference = userPreferenceQuery.findByUser(user);

        // 생년월일 업데이트시 20~40세인 지 검증, 새로운 나이 계산
        LocalDate birth = request.getBirth();
        LocalDate currentDate = LocalDate.now();
        AgeValidator.validateAgeByBirth(birth, currentDate);
        int age = Period.between(birth, currentDate).getYears();

        // 음역대, 선호하는 음역대 조회
        List<VocalRange> vocalRanges = vocalRangeQuery.findAll();
        VocalRange vocalRange =
            findVocalRangeByClassification(vocalRanges, request.getVocalRange());
        VocalRange preferredVocalRange =
            findVocalRangeByClassification(vocalRanges, request.getPreferredVocalRange());

        // 동물상, 선호하는 동물상 조회
        List<Animal> animals = animalQuery.findAll();
        Animal animal = findAnimalByName(animals, request.getLookAlikeAnimal());
        Animal preferredAnimal = findAnimalByName(animals, request.getPreferredAnimal());

        // 활동 지역, 선호하는 지역 조회
        List<Area> areas = areaQuery.findAll();
        Area area = findAreaByName(areas, request.getActivityArea());
        Area preferredArea = findAreaByName(areas, request.getPreferredArea());

        // 하한, 상한 값이 모두 존재할 경우만 하한 <= 상한 검증
        Integer preferredAgeLowerBound = request.getPreferredAgeLowerBound();
        Integer preferredAgeUpperBound = request.getPreferredAgeUpperBound();
        BoundsValidator.validateBounds(preferredAgeLowerBound, preferredAgeUpperBound);

        Integer preferredHeightLowerBound = request.getPreferredHeightLowerBound();
        Integer preferredHeightUpperBound = request.getPreferredHeightUpperBound();
        BoundsValidator.validateBounds(preferredHeightLowerBound, preferredHeightUpperBound);

        // 사용자 프로필 및 선호 조건 업데이트
        user.updateProfile(
            request.getNickname(),
            request.getHeight(),
            birth,
            age,
            request.getBodyType(),
            request.getJob(),
            request.getMbti(),
            vocalRange,
            animal,
            area);

        userPreference.updatePreference(
            preferredAgeLowerBound,
            preferredAgeUpperBound,
            preferredHeightLowerBound,
            preferredHeightUpperBound,
            request.getPreferredMbti(),
            request.getPreferredBodyType(),
            preferredVocalRange,
            preferredAnimal,
            preferredArea);
    }

    private VocalRange findVocalRangeByClassification(
        List<VocalRange> vocalRanges, String classification) {

        return vocalRanges.stream()
            .filter(vocalRange -> vocalRange.hasSameClassificationAs(classification))
            .findFirst()
            .orElseThrow(() -> new GeneralException(UserException.VOCAL_RANGE_NOT_FOUND));
    }

    private Animal findAnimalByName(List<Animal> animals, String name) {

        return animals.stream()
            .filter(animal -> animal.hasSameNameAs(name))
            .findFirst()
            .orElseThrow(() -> new GeneralException(UserException.ANIMAL_NOT_FOUND));
    }

    private Area findAreaByName(List<Area> areas, String name) {

        return areas.stream()
            .filter(area -> area.hasSameNameAs(name))
            .findFirst()
            .orElseThrow(() -> new GeneralException(UserException.AREA_NOT_FOUND));
    }

    @Transactional
    public void updateProfilePhotos(long userId, UserUpdateProfilePhotoRequest request) {

        User user = userQuery.findById(userId);

        // 사진 업로드 및 사진 싱크로율 업데이트
        s3Service.uploadProfilePhotos(request.getProfilePhotos(), user);
        user.updatePhotoSyncRate(request.getPhotoSyncRate());
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

        Optional<Block> block = blockQuery.findByUser(user, blockedUser);

        if (block.isEmpty()) {
            throw new GeneralException(BlockException.NOT_BLOCKED_USER);
        }

        blockCommand.delete(block.get());
    }

    @Transactional
    public void changeInactiveStatus(long userId, UserChangeInactiveStatusRequest request) {

        User user = userQuery.findById(userId);
        user.changeInactiveStatus(request.isInactive());
    }

    @Transactional
    public void deleteUser(long userId) {

        User user = userQuery.findById(userId);
        user.delete();
    }

    @Transactional(readOnly = true)
    public NotificationPermissionsResponse getNotificationPermissions(long userId) {

        User user = userQuery.findById(userId);
        List<NotificationPermission> notificationPermissions =
            notificationPermissionQuery.findAllByUser(user);

        List<NotificationPermissionDetail> permissions = notificationPermissions.stream()
            .map(NotificationPermissionDetail::of)
            .toList();

        return new NotificationPermissionsResponse(permissions);
    }

    @Transactional
    public void updateNotificationPermissions(
        long userId,
        NotificationPermissionsUpdateRequest request) {

        // 요청 동의 내역 리스트, null 값 포함 여부 검증
        List<NotificationPermissionDetail> permissions = request.getPermissions();
        if (permissions.contains(null)) {
            throw new GeneralException(
                NotificationException.REQUEST_PERMISSIONS_CAN_NOT_CONTAIN_NULL);
        }

        // 사용자 및 사용자 동의 내역 목록 조회
        User user = userQuery.findById(userId);
        List<NotificationPermission> notificationPermissions =
            notificationPermissionQuery.findAllByUser(user);

        // 요청 내역과 알림 타입 일치하는 동의 내역 업데이트
        permissions.forEach(permission -> {
            NotificationType type = NotificationType.valueOf(permission.getType());

            notificationPermissions.forEach(notificationPermission -> {
                if (notificationPermission.hasSameTypeAs(type)) {
                    notificationPermission.updatePermissionStatus(permission.isPermit());
                }
            });
        });
    }
}
