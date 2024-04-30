package yeonba.be.mypage.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
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
        LocalDate currentDate = LocalDate.now();
        if (AgeValidator.isNotAdult(birth, currentDate)) {
            throw new GeneralException(UserException.IS_NOT_ADULT);
        }
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
        validateBounds(preferredAgeLowerBound, preferredAgeUpperBound);

        Integer preferredHeightLowerBound = request.getPreferredHeightLowerBound();
        Integer preferredHeightUpperBound = request.getPreferredHeightUpperBound();
        validateBounds(preferredHeightLowerBound, preferredHeightUpperBound);

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

    private void validateBounds(Integer lowerBound, Integer upperBound) {

        if (Objects.isNull(lowerBound) || Objects.isNull(upperBound)) {

            return;
        }

        if (lowerBound > upperBound) {
            throw new GeneralException(UserException.LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND);
        }
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
