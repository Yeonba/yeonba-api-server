package yeonba.be.mypage.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.mypage.dto.request.UserChangePasswordRequest;
import yeonba.be.mypage.dto.request.UserDormantRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.BlockedUserResponse;
import yeonba.be.mypage.dto.response.BlockedUsersResponse;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;
import yeonba.be.user.entity.VocalRange;
import yeonba.be.user.repository.BlockCommand;
import yeonba.be.user.repository.BlockQuery;
import yeonba.be.user.repository.UserQuery;
import yeonba.be.user.repository.animal.AnimalQuery;
import yeonba.be.user.repository.area.AreaQuery;
import yeonba.be.user.repository.userpreference.UserPreferenceQuery;
import yeonba.be.user.repository.vocalrange.VocalRangeQuery;
import yeonba.be.util.AgeValidator;
import yeonba.be.util.PasswordEncryptor;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final AnimalQuery animalQuery;
    private final AreaQuery areaQuery;
    private final BlockQuery blockQuery;
    private final UserPreferenceQuery userPreferenceQuery;
    private final UserQuery userQuery;
    private final VocalRangeQuery vocalRangeQuery;

    private final BlockCommand blockCommand;

    private final PasswordEncryptor passwordEncryptor;
    private final S3Client s3Client;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    @Transactional(readOnly = true)
    public UserSimpleProfileResponse getSimpleProfile(long userId) {

        User user = userQuery.findById(userId);

        return new UserSimpleProfileResponse(
            user.getName(),
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

        // 사용자 및 사용자 선호 조건 조회
        User user = userQuery.findById(userId);
        UserPreference userPreference = userPreferenceQuery.findByUser(user);

        // 생년월일 업데이트시 성인(만 18세 이상)인 지 검증, 새로운 나이 계산
        LocalDate birth = request.getBirth();
        LocalDate now = LocalDate.now();
        if(AgeValidator.isNotAdult(birth, now)) {
            throw new GeneralException(UserException.IS_NOT_ADULT);
        }
        int age = (int) ChronoUnit.YEARS.between(birth, now);

        // 음역대, 선호하는 음역대 조회
        VocalRange vocalRange = vocalRangeQuery.findBy(request.getVocalRange());
        VocalRange preferredVocalRange = vocalRangeQuery.findBy(request.getPreferredVocalRange());

        // 동물상, 선호하는 동물상 조회
        Animal animal = animalQuery.findByName(request.getLookAlikeAnimal());
        Animal preferredAnimal = animalQuery.findByName(request.getPreferredAnimal());

        // 활동 지역, 선호하는 지역 조회
        Area area = areaQuery.findByName(request.getActivityArea());
        Area preferredArea = areaQuery.findByName(request.getPreferredArea());

        // 선호하는 나이 하한 <= 상한 검증
        int preferredAgeLowerBound = request.getPreferredAgeLowerBound();
        int preferredAgeUpperBound = request.getPreferredAgeUpperBound();
        if(preferredAgeUpperBound < preferredAgeLowerBound) {
            throw new GeneralException(UserException.LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND);
        }

        // 선호하는 키 하한 <= 상한 검증
        int preferredHeightLowerBound = request.getPreferredHeightLowerBound();
        int preferredHeightUpperBound = request.getPreferredHeightUpperBound();
        if(preferredHeightUpperBound < preferredHeightLowerBound) {
            throw new GeneralException(UserException.LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND);
        }

        // 사용자 프로필 및 선호 조건 업데이트
        user.updateProfile(birth, age, vocalRange, animal, area);
        userPreference.updatePreference(
            preferredVocalRange,
            preferredAnimal,
            preferredArea,
            preferredAgeLowerBound,
            preferredHeightUpperBound,
            preferredHeightLowerBound,
            preferredHeightUpperBound);
    }

    @Transactional
    public void changePassword(UserChangePasswordRequest request, long userId) {

        User user = userQuery.findById(userId);

        String encryptedOldPassword = passwordEncryptor
            .encrypt(request.getOldPassword(), user.getSalt());

        comparePasswords(request, user, encryptedOldPassword);

        String encryptedNewPassword = passwordEncryptor
            .encrypt(request.getNewPassword(), user.getSalt());

        user.changePassword(encryptedNewPassword);
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

    /**
     * 기존 비밀번호가 올바른지 검증 새 비밀번호와 새 비밀번호 확인 값이 일치하는지 검증
     */
    private void comparePasswords(UserChangePasswordRequest request,
        User user,
        String encryptedOldPassword) {

        if (!user.getEncryptedPassword().equalsIgnoreCase(encryptedOldPassword)) {
            throw new IllegalArgumentException("기존 비밀번호가 틀렸습니다.");
        }

        if (!StringUtils.equals(request.getNewPassword(),
            request.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인 값이 일치하지 않습니다.");
        }
    }
}
