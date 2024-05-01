package yeonba.be.user.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.JoinException;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.user.dto.response.UserProfileResponse;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.ProfilePhoto;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;
import yeonba.be.user.entity.VocalRange;
import yeonba.be.user.enums.Gender;
import yeonba.be.user.enums.LoginType;
import yeonba.be.user.repository.animal.AnimalQuery;
import yeonba.be.user.repository.area.AreaQuery;
import yeonba.be.user.repository.profilephoto.ProfilePhotoCommand;
import yeonba.be.user.repository.user.UserCommand;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.user.repository.userpreference.UserPreferenceCommand;
import yeonba.be.user.repository.vocalrange.VocalRangeQuery;
import yeonba.be.util.AgeValidator;
import yeonba.be.util.S3Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final int JOIN_REWARD_ARROWS = 30;

    private final ProfilePhotoCommand profilePhotoCommand;
    private final UserCommand userCommand;
    private final UserPreferenceCommand userPreferenceCommand;

    private final AnimalQuery animalQuery;
    private final AreaQuery areaQuery;
    private final ArrowQuery arrowQuery;
    private final UserQuery userQuery;
    private final VocalRangeQuery vocalRangeQuery;

    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public UserProfileResponse getTargetUserProfile(long userId, long targetUserId) {

        User user = userQuery.findById(userId);
        User targetUser = userQuery.findById(targetUserId);

        boolean isAlreadySentArrow = arrowQuery.isArrowTransactionExist(user, targetUser);

        return new UserProfileResponse(
            targetUser.getProfilePhotoUrls(),
            targetUser.getGender(),
            targetUser.getNickname(),
            targetUser.getArrow(),
            targetUser.getAge(),
            targetUser.getHeight(),
            targetUser.getArea().getName(),
            targetUser.getPhotoSyncRate(),
            targetUser.getVocalRange().getClassification(),
            targetUser.getAnimal().getName(),
            isAlreadySentArrow);
    }

    public User saveUser(UserJoinRequest request) {

        LoginType loginType = LoginType.from(request.getLoginType());

        // 이미 사용 중인 닉네임인지 확인
        if (userQuery.validateUsedNickname(request.getNickname())) {

            throw new GeneralException(JoinException.ALREADY_USED_NICKNAME);
        }

        // 이미 사용 중인 핸드폰 번호인지 확인
        if (userQuery.validateUsedPhoneNumber(request.getPhoneNumber())) {

            throw new GeneralException(JoinException.ALREADY_USED_PHONE_NUMBER);
        }

        // 성별 판별
        Gender gender = Gender.from(request.getGender());

        // 나이 20~40세인 지 검증 & 나이 계산
        LocalDate birth = request.getBirth();
        LocalDate currentDate = LocalDate.now();
        AgeValidator.validateAgeByBirth(birth, currentDate);
        int age = Period.between(birth, currentDate).getYears();

        // 음역대, 동물상, 지역 조회
        VocalRange vocalRange = vocalRangeQuery.findByClassification(request.getVocalRange());
        Animal animal = animalQuery.findByName(request.getLookAlikeAnimal());
        Area area = areaQuery.findByName(request.getActivityArea());

        // 사용자 생성 및 저장
        User user = new User(
            request.getSocialId(),
            loginType,
            gender.genderBoolean,
            request.getName(),
            request.getNickname(),
            request.getBirth(),
            age,
            request.getHeight(),
            request.getPhoneNumber(),
            JOIN_REWARD_ARROWS,
            request.getPhotoSyncRate(),
            request.getBodyType(),
            request.getJob(),
            request.getMbti(),
            vocalRange,
            animal,
            area);

        return userCommand.save(user);
    }

    public void saveProfilePhotos(User user, UserJoinRequest request) {

        List<MultipartFile> photoFiles = request.getProfilePhotos();

        List<String> profilePhotoUrls = s3Service.uploadProfilePhotos(photoFiles, user);
        List<ProfilePhoto> profilePhotos = profilePhotoUrls.stream()
            .map(profilePhotoUrl -> new ProfilePhoto(user, profilePhotoUrl))
            .toList();

        profilePhotoCommand.save(profilePhotos);
        user.updateProfilePhotos(profilePhotos);
    }

    public void saveUserPreference(User user, UserJoinRequest request) {

        // 선호 음역대, 동물상, 지역 조회
        Animal preferredAnimal = animalQuery.findByName(request.getPreferredAnimal());
        VocalRange preferredVocalRange =
            vocalRangeQuery.findByClassification(request.getPreferredVocalRange());
        Area preferredArea = areaQuery.findByName(request.getPreferredArea());

        UserPreference userPreference = new UserPreference(
            request.getPreferredAgeLowerBound(),
            request.getPreferredAgeUpperBound(),
            request.getPreferredHeightLowerBound(),
            request.getPreferredHeightUpperBound(),
            request.getMbti(),
            request.getBodyType(),
            user,
            preferredVocalRange,
            preferredArea,
            preferredAnimal);
        userPreferenceCommand.save(userPreference);
    }
}
