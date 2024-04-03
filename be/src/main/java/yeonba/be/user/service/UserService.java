package yeonba.be.user.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.JoinException;
import yeonba.be.exception.UserException;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.user.dto.request.UserQueryRequest;
import yeonba.be.user.dto.response.UserProfileResponse;
import yeonba.be.user.dto.response.UserQueryPageResponse;
import yeonba.be.user.dto.response.UserQueryResponse;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.ProfilePhoto;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;
import yeonba.be.user.entity.UserRecommendation;
import yeonba.be.user.entity.VocalRange;
import yeonba.be.user.enums.Gender;
import yeonba.be.user.repository.UserCommand;
import yeonba.be.user.repository.UserQuery;
import yeonba.be.user.repository.animal.AnimalQuery;
import yeonba.be.user.repository.area.AreaQuery;
import yeonba.be.user.repository.profilephoto.ProfilePhotoCommand;
import yeonba.be.user.repository.userpreference.UserPreferenceCommand;
import yeonba.be.user.repository.userrecommendation.UserRecommendationCommand;
import yeonba.be.user.repository.userrecommendation.UserRecommendationQuery;
import yeonba.be.user.repository.vocalrange.VocalRangeQuery;
import yeonba.be.util.PasswordEncryptor;
import yeonba.be.util.S3Service;
import yeonba.be.util.SaltGenerator;

@Service
@RequiredArgsConstructor
public class UserService {

    private final int JOIN_REWARD_ARROWS = 30;
    private final int DEFAULT_PAGE_SIZE = 6;
    private final int RECOMMEND_USERS_PAGE_SIZE = 2;
    private final int ARROWS_FOR_RECOMMEND = 5;

    private final ProfilePhotoCommand profilePhotoCommand;
    private final UserCommand userCommand;
    private final UserPreferenceCommand userPreferenceCommand;
    private final UserRecommendationCommand userRecommendationCommand;

    private final AnimalQuery animalQuery;
    private final AreaQuery areaQuery;
    private final ArrowQuery arrowQuery;
    private final UserQuery userQuery;
    private final UserRecommendationQuery userRecommendationQuery;
    private final VocalRangeQuery vocalRangeQuery;

    private final PasswordEncryptor passwordEncryptor;
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

    @Transactional
    public User saveUser(UserJoinRequest request) {

        // 이미 사용 중인 이메일인지 확인
        if (userQuery.isAlreadyUsedEmail(request.getEmail())) {
            throw new GeneralException(JoinException.ALREADY_USED_EMAIL);
        }

        // 이미 사용 중인 닉네임인지 확인
        if (userQuery.isAlreadyUsedNickname(request.getNickname())) {
            throw new GeneralException(JoinException.ALREADY_USED_NICKNAME);
        }

        // 비밀빈호, 비밀번호 확인 값 일치 확인
        String password = request.getPassword();
        String passwordConfirmation = request.getPasswordConfirmation();
        if (!StringUtils.equals(password, passwordConfirmation)) {
            throw new GeneralException(JoinException.PASSWORD_CONFIRMATION_NOT_MATCH);
        }

        // 성별 판별
        Gender gender = Gender.from(request.getGender());

        // 나이 계산
        LocalDate birth = request.getBirth();
        int age = Period.between(birth, LocalDate.now()).getYears();

        // salt 생성 및 비밀번호 암호화
        String salt = SaltGenerator.generateRandomSalt();
        String encryptedPassword = passwordEncryptor.encrypt(password, salt);

        // 음역대, 동물상, 지역 조회
        VocalRange vocalRange = vocalRangeQuery.findBy(request.getVocalRange());
        Animal animal = animalQuery.findByName(request.getLookAlikeAnimal());
        Area area = areaQuery.findByName(request.getActivityArea());

        // 사용자 생성 및 저장
        User user = new User(
            gender.genderBoolean,
            request.getName(),
            request.getNickname(),
            request.getBirth(),
            age,
            request.getHeight(),
            request.getEmail(),
            encryptedPassword,
            salt,
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

    @Transactional
    public void saveProfilePhotos(User user, UserJoinRequest request) {

        List<MultipartFile> photoFiles = request.getProfilePhotos();

        List<String> profilePhotoUrls = s3Service.uploadProfilePhotos(photoFiles, user);
        List<ProfilePhoto> profilePhotos = profilePhotoUrls.stream()
            .map(profilePhotoUrl -> new ProfilePhoto(user, profilePhotoUrl))
            .toList();

        profilePhotoCommand.save(profilePhotos);
        user.updateProfilePhotos(profilePhotos);
    }

    @Transactional
    public void saveUserPreference(User user, UserJoinRequest request) {

        // 선호 음역대, 동물상, 지역 조회
        Animal preferredAnimal = animalQuery.findByName(request.getPreferredAnimal());
        VocalRange preferredVocalRange = vocalRangeQuery.findBy(request.getPreferredVocalRange());
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

    // controller에서 type에 대해 검증, 페이지 사이즈가 6으로 같은 경우 조회 로직
    @Transactional(readOnly = true)
    public UserQueryPageResponse findByQueryCondition(long userId, UserQueryRequest request) {

        String type = request.getType();
        int page = request.getPage();
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        if (StringUtils.equals(type, "BOOKMARKED")) {

            return findAllFavorites(userId, pageRequest);
        }

        if (StringUtils.equals(type, "ARROW_RECEIVERS")) {

            return findAllArrowReceivers(userId, pageRequest);
        }

        return findAllArrowSenders(userId, pageRequest);
    }

    private UserQueryPageResponse findAllFavorites(long userId, PageRequest pageRequest) {

        return userQuery.findAllFavorites(userId, pageRequest);
    }

    private UserQueryPageResponse findAllArrowReceivers(long senderId, PageRequest pageRequest) {

        return userQuery.findAllArrowReceivers(senderId, pageRequest);
    }

    private UserQueryPageResponse findAllArrowSenders(long receiverId, PageRequest pageRequest) {

        return userQuery.findAllArrowSenders(receiverId, pageRequest);
    }

    @Transactional
    public UserQueryPageResponse findRecommendUsers(long userId, UserQueryRequest request) {

        User user = userQuery.findById(userId);
        int page = request.getPage();
        PageRequest pageRequest = PageRequest.of(page, RECOMMEND_USERS_PAGE_SIZE);
        LocalDate recommendDate = LocalDate.now();

        // 한 번 추천받았을 경우 다음 시도부턴 화살 소모
        if (userRecommendationQuery.isRecommendationInSameDateExistBy(user, recommendDate)) {

            user.minusArrow(ARROWS_FOR_RECOMMEND);
        }

        // 추천 사용자 응답 조회
        UserQueryPageResponse response = userQuery
            .findRecommendUsers(userId, pageRequest, recommendDate);

        // 추천 가능 여부 확인(추천 가능한 사용자 2명 이상)
        List<UserQueryResponse> content = response.getUsers();
        if (content.size() < RECOMMEND_USERS_PAGE_SIZE) {

            throw new GeneralException(UserException.NO_MORE_USERS_TO_RECOMMEND);
        }

        // 추천 사용자 조회
        List<Long> userIds = content.stream()
            .map(UserQueryResponse::getId)
            .toList();
        List<User> recommendUsers = userQuery.findByIds(userIds);

        // 추천 내역 저장
        List<UserRecommendation> userRecommendations = recommendUsers.stream()
            .map(recommendUser -> new UserRecommendation(user, recommendUser))
            .toList();
        userRecommendationCommand.saveAll(userRecommendations);

        return response;
    }
}
