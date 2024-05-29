package yeonba.be.user.service;

import static yeonba.be.util.BoundsValidator.validateBounds;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.chatting.repository.chatroom.ChatRoomQuery;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.JoinException;
import yeonba.be.exception.UserException;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.user.dto.request.UserQueryRequest;
import yeonba.be.user.dto.request.UserSearchRequest;
import yeonba.be.user.dto.request.UserUpdateDeviceTokenRequest;
import yeonba.be.user.dto.response.UserProfileResponse;
import yeonba.be.user.dto.response.UserQueryPageResponse;
import yeonba.be.user.dto.response.UserQueryResponse;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.ProfilePhoto;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;
import yeonba.be.user.entity.UserRecommendation;
import yeonba.be.user.entity.UserSearchLog;
import yeonba.be.user.entity.VocalRange;
import yeonba.be.user.enums.Gender;
import yeonba.be.user.enums.LoginType;
import yeonba.be.user.repository.animal.AnimalQuery;
import yeonba.be.user.repository.area.AreaQuery;
import yeonba.be.user.repository.profilephoto.ProfilePhotoCommand;
import yeonba.be.user.repository.user.UserCommand;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.user.repository.userpreference.UserPreferenceCommand;
import yeonba.be.user.repository.userpreference.UserPreferenceQuery;
import yeonba.be.user.repository.userrecommendation.UserRecommendationCommand;
import yeonba.be.user.repository.userrecommendation.UserRecommendationQuery;
import yeonba.be.user.repository.usersearchlog.UserSearchLogCommand;
import yeonba.be.user.repository.vocalrange.VocalRangeQuery;
import yeonba.be.util.AgeValidator;
import yeonba.be.util.S3Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProfilePhotoCommand profilePhotoCommand;
    private final UserCommand userCommand;
    private final UserPreferenceCommand userPreferenceCommand;
    private final UserRecommendationCommand userRecommendationCommand;
    private final UserSearchLogCommand userSearchLogCommand;

    private final AnimalQuery animalQuery;
    private final AreaQuery areaQuery;
    private final ArrowQuery arrowQuery;
    private final ChatRoomQuery chatRoomQuery;
    private final UserQuery userQuery;
    private final UserPreferenceQuery userPreferenceQuery;
    private final UserRecommendationQuery userRecommendationQuery;
    private final VocalRangeQuery vocalRangeQuery;

    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public UserProfileResponse getTargetUserProfile(long userId, long targetUserId) {

        User user = userQuery.findById(userId);

        // 조회하는 사용자 정보, 선호조건, 이전 화살 송신 여부 조회
        User targetUser = userQuery.findById(targetUserId);
        UserPreference targetUserPreference = userPreferenceQuery.findByUser(targetUser);
        boolean isAlreadySentArrow = arrowQuery.isArrowTransactionExist(user, targetUser);
        boolean chatRoomExist =
            chatRoomQuery.existsBy(user, targetUser) || chatRoomQuery.existsBy(targetUser, user);

        return UserProfileResponse.from(targetUser, targetUserPreference, isAlreadySentArrow,
            !chatRoomExist);
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
        int joinRewardArrows = 30;
        User user = new User(
            request.getSocialId(),
            loginType,
            gender.genderBoolean,
            request.getNickname(),
            request.getBirth(),
            age,
            request.getHeight(),
            request.getPhoneNumber(),
            joinRewardArrows,
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

    @Transactional(readOnly = true)
    public UserQueryPageResponse findUsersByQueryCondition(long userId, UserQueryRequest request) {

        int page = Optional.ofNullable(request.getPage()).orElse(0);
        int size = 30;
        PageRequest pageRequest = PageRequest.of(page, size);

        // 사용자 존재 여부 검증
        if (!userQuery.validateExistsById(userId)) {
            throw new GeneralException(UserException.USER_NOT_FOUND);
        }

        String type = request.getType();
        if (StringUtils.equals(type, "FAVORITES")) {

            return userQuery.findFavoritesBy(userId, pageRequest);
        }

        if (StringUtils.equals(type, "ARROW_RECEIVERS")) {

            return userQuery.findArrowReceiversBy(userId, pageRequest);
        }

        return userQuery.findArrowSendersBy(userId, pageRequest);
    }

    @Transactional
    public UserQueryPageResponse findRecommendUsers(long userId, LocalDate recommendDay) {

        User user = userQuery.findById(userId);

        // 한 번 추천받았을 경우 다음 시도부턴 화살 소모
        int arrowsForRecommend = 5;
        if (userRecommendationQuery.existsRecommendationForUserOnDay(user, recommendDay)) {
            user.minusArrow(arrowsForRecommend);
        }

        // 추천 사용자 응답 조회,
        int numberOfRecommendUsers = 2;
        PageRequest pageRequest = PageRequest.of(0, numberOfRecommendUsers);
        UserQueryPageResponse response = userQuery
            .findRecommendUsers(user, pageRequest, recommendDay);

        // 추천 가능 여부 확인(추천 가능한 사용자 2명 이상)
        List<UserQueryResponse> content = response.getUsers();
        if (content.size() < numberOfRecommendUsers) {
            throw new GeneralException(UserException.NO_MORE_USERS_TO_RECOMMEND);
        }

        // 추천 사용자 조회
        List<User> recommendUsers = findAllUsersInResponse(response);

        // 추천 내역 저장
        List<UserRecommendation> userRecommendations = recommendUsers.stream()
            .map(recommendUser -> new UserRecommendation(user, recommendUser))
            .toList();
        userRecommendationCommand.saveAll(userRecommendations);

        return response;
    }

    @Transactional
    public void updateDeviceToken(long userId, UserUpdateDeviceTokenRequest request) {

        User user = userQuery.findById(userId);
        user.updateDeviceToken(request.getDeviceToken());
    }

    @Transactional
    public UserQueryPageResponse findUsersBySearchCondition(long userId,
        UserSearchRequest request) {

        int page = 0;
        if (Objects.nonNull(request) && Objects.nonNull(request.getPage())) {
            page = request.getPage();

            // 검색 나이/키 하한 <= 상한 여부 검증
            validateBounds(request.getAgeLowerBound(), request.getAgeUpperBound());
            validateBounds(request.getHeightLowerBound(), request.getHeightUpperBound());
        }

        int size = 30;
        PageRequest pageRequest = PageRequest.of(page, size);
        LocalDate searchDay = LocalDate.now();

        // 검색하는 사용자 조회
        User user = userQuery.findById(userId);

        // 응답 조회
        UserQueryPageResponse response = userQuery
            .findUsersBySearchCondition(user, pageRequest, searchDay, request);

        // 검색된 사용자 조회
        List<User> searchingUsers = findAllUsersInResponse(response);

        // 검색 내역 저장
        List<UserSearchLog> userSearchLogs = searchingUsers.stream()
            .map(searchingUser -> new UserSearchLog(user, searchingUser))
            .toList();
        userSearchLogCommand.saveAll(userSearchLogs);

        return response;
    }

    private List<User> findAllUsersInResponse(UserQueryPageResponse response) {

        List<Long> userIds = response.getUsers().stream()
            .map(UserQueryResponse::getId)
            .toList();

        return userQuery.findByIds(userIds);
    }
}
