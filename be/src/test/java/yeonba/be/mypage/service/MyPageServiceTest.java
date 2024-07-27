package yeonba.be.mypage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static yeonba.be.mypage.fixtures.UserFixtures.AGE_LOWER_BOUND;
import static yeonba.be.mypage.fixtures.UserFixtures.AGE_UPPER_BOUND;
import static yeonba.be.mypage.fixtures.UserFixtures.ANIMAL;
import static yeonba.be.mypage.fixtures.UserFixtures.AREA;
import static yeonba.be.mypage.fixtures.UserFixtures.BIRTH;
import static yeonba.be.mypage.fixtures.UserFixtures.BODY_TYPE;
import static yeonba.be.mypage.fixtures.UserFixtures.HEIGHT;
import static yeonba.be.mypage.fixtures.UserFixtures.HEIGHT_LOWER_BOUND;
import static yeonba.be.mypage.fixtures.UserFixtures.HEIGHT_UPPER_BOUND;
import static yeonba.be.mypage.fixtures.UserFixtures.JOB;
import static yeonba.be.mypage.fixtures.UserFixtures.MBTI;
import static yeonba.be.mypage.fixtures.UserFixtures.NICKNAME;
import static yeonba.be.mypage.fixtures.UserFixtures.PHOTO_SYNC_RATE;
import static yeonba.be.mypage.fixtures.UserFixtures.USER_ID;
import static yeonba.be.mypage.fixtures.UserFixtures.VOCAL_RANGE;
import static yeonba.be.mypage.fixtures.UserFixtures.user;
import static yeonba.be.mypage.fixtures.UserFixtures.userPreference;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.exception.UtilException;
import yeonba.be.mypage.dto.request.UserUpdateProfilePhotoRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;
import yeonba.be.user.repository.animal.AnimalQuery;
import yeonba.be.user.repository.area.AreaQuery;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.user.repository.userpreference.UserPreferenceQuery;
import yeonba.be.user.repository.vocalrange.VocalRangeQuery;
import yeonba.be.util.S3Service;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @Mock
    private UserQuery userQuery;

    @Mock
    private UserPreferenceQuery userPreferenceQuery;

    @Mock
    private VocalRangeQuery vocalRangeQuery;

    @Mock
    private AnimalQuery animalQuery;

    @Mock
    private AreaQuery areaQuery;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MyPageService myPageService;

    @Nested
    @DisplayName("사용자는 자신의 프로필 조회 시 ")
    class GetSimpleProfileTest {

        @DisplayName("조회에 성공한다.")
        @Test
        void success() {
            // given
            User user = user();
            given(userQuery.findById(USER_ID)).willReturn(user);

            // when
            UserSimpleProfileResponse response = myPageService.getSimpleProfile(USER_ID);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getName()).isEqualTo(user.getNickname());
                softly.assertThat(response.getProfileImageUrl())
                    .isEqualTo(user.getRepresentativeProfilePhoto());
                softly.assertThat(response.getArrows()).isEqualTo(user.getArrow());
            });
        }

        @DisplayName("존재하지 않는 사용자인 경우 예외가 발생한다.")
        @Test
        void userNotFound() {
            // given
            given(userQuery.findById(USER_ID))
                .willThrow(new GeneralException(UserException.USER_NOT_FOUND));

            // when
            assertThatThrownBy(() -> myPageService.getSimpleProfile(USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.USER_NOT_FOUND);

            // then

        }
    }

    @Nested
    @DisplayName("사용자는 자신의 상세 프로필 조회 시 ")
    class GetProfileDetailTest {

        @DisplayName("조회에 성공한다.")
        @Test
        void success() {
            // given
            User user = user();
            given(userQuery.findById(USER_ID)).willReturn(user);

            UserPreference userPreference = userPreference();
            given(userPreferenceQuery.findByUser(user)).willReturn(userPreference);

            // when
            UserProfileDetailResponse response = myPageService.getProfileDetail(USER_ID);

            // then
            SoftAssertions.assertSoftly(softly -> {
                List<String> profilePhotoUrls = response.getProfilePhotoUrls();
                softly.assertThat(profilePhotoUrls).hasSize(2);
                softly.assertThat(profilePhotoUrls).isEqualTo(user.getProfilePhotoUrls());
                softly.assertThat(response.getGender()).isEqualTo(user.getGenderString());
                softly.assertThat(response.getBirth()).isEqualTo(user.getBirth());
                softly.assertThat(response.getHeight()).isEqualTo(user.getHeight());
                softly.assertThat(response.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
                softly.assertThat(response.getNickname()).isEqualTo(user.getNickname());
                softly.assertThat(response.getPhotoSyncRate()).isEqualTo(user.getPhotoSyncRate());
                softly.assertThat(response.getJob()).isEqualTo(user.getJob());
                softly.assertThat(response.getMbti()).isEqualTo(user.getMbti());
                softly.assertThat(response.getVocalRange())
                    .isEqualTo(user.getVocalRange().getClassification());
                softly.assertThat(response.getLookAlikeAnimal())
                    .isEqualTo(user.getAnimal().getName());
                softly.assertThat(response.getActivityArea()).isEqualTo(user.getArea().getName());

                softly.assertThat(response.getPreferredVocalRange())
                    .isEqualTo(userPreference.getVocalRange().getClassification());
                softly.assertThat(response.getPreferredAnimal())
                    .isEqualTo(userPreference.getAnimal().getName());
                softly.assertThat(response.getPreferredArea())
                    .isEqualTo(userPreference.getArea().getName());
                softly.assertThat(response.getPreferredAgeLowerBound())
                    .isEqualTo(userPreference.getAgeLowerBound());
                softly.assertThat(response.getPreferredAgeUpperBound())
                    .isEqualTo(userPreference.getAgeUpperBound());
                softly.assertThat(response.getPreferredHeightLowerBound())
                    .isEqualTo(userPreference.getHeightLowerBound());
                softly.assertThat(response.getPreferredHeightUpperBound())
                    .isEqualTo(userPreference.getHeightUpperBound());
                softly.assertThat(response.getPreferredMbti()).isEqualTo(userPreference.getMbti());
                softly.assertThat(response.getPreferredBodyType())
                    .isEqualTo(userPreference.getBodyType());
            });
        }

        @DisplayName("사용자가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void userNotFound() {
            // given
            given(userQuery.findById(USER_ID)).willThrow(
                new GeneralException(UserException.USER_NOT_FOUND));

            // when
            assertThatThrownBy(() -> myPageService.getProfileDetail(USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.USER_NOT_FOUND);

            // then

        }
    }

    @Nested
    @DisplayName("사용자는 자신의 프로필 수정 시 ")
    class UpdateProfileTest {

        @DisplayName("수정에 성공한다.")
        @Test
        void updateProfile() {
            // given
            String updateVocalRange = VOCAL_RANGE.getClassification();
            String updateArea = AREA.getName();
            String updateAnimal = ANIMAL.getName();

            User user = user();
            given(userQuery.findById(USER_ID)).willReturn(user);

            UserPreference userPreference = userPreference();
            given(userPreferenceQuery.findByUser(user)).willReturn(userPreference);

            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));
            given(animalQuery.findAll()).willReturn(List.of(ANIMAL));
            given(areaQuery.findAll()).willReturn(List.of(AREA));

            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                updateVocalRange,
                BIRTH,
                BODY_TYPE,
                JOB,
                updateArea,
                updateAnimal,
                MBTI,
                updateAnimal,
                updateArea,
                updateVocalRange,
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            myPageService.updateProfile(request, USER_ID);

            // then
            int updateAge = Period.between(request.getBirth(), LocalDate.now()).getYears();

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(user.getNickname()).isEqualTo(request.getNickname());
                softly.assertThat(user.getHeight()).isEqualTo(request.getHeight());
                softly.assertThat(user.getBirth()).isEqualTo(request.getBirth());
                softly.assertThat(user.getAge()).isEqualTo(updateAge);
                softly.assertThat(user.getBodyType()).isEqualTo(request.getBodyType());
                softly.assertThat(user.getJob()).isEqualTo(request.getJob());
                softly.assertThat(user.getMbti()).isEqualTo(request.getMbti());
                softly.assertThat(user.getVocalRange().getClassification())
                    .isEqualTo(request.getVocalRange());
                softly.assertThat(user.getAnimal().getName())
                    .isEqualTo(request.getLookAlikeAnimal());
                softly.assertThat(user.getArea().getName()).isEqualTo(request.getActivityArea());

                softly.assertThat(userPreference.getAgeLowerBound())
                    .isEqualTo(request.getPreferredAgeLowerBound());
                softly.assertThat(userPreference.getAgeUpperBound())
                    .isEqualTo(request.getPreferredAgeUpperBound());
                softly.assertThat(userPreference.getHeightLowerBound())
                    .isEqualTo(request.getPreferredHeightLowerBound());
                softly.assertThat(userPreference.getHeightUpperBound())
                    .isEqualTo(request.getPreferredHeightUpperBound());
                softly.assertThat(userPreference.getMbti()).isEqualTo(request.getPreferredMbti());
                softly.assertThat(userPreference.getBodyType())
                    .isEqualTo(request.getPreferredBodyType());
                softly.assertThat(userPreference.getVocalRange().getClassification())
                    .isEqualTo(request.getPreferredVocalRange());
                softly.assertThat(userPreference.getAnimal().getName())
                    .isEqualTo(request.getPreferredAnimal());
                softly.assertThat(userPreference.getArea().getName())
                    .isEqualTo(request.getPreferredArea());
            });
        }

        @DisplayName("사용자가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void userNotFound() {
            // given
            given(userQuery.findById(USER_ID))
                .willThrow(new GeneralException(UserException.USER_NOT_FOUND));

            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.USER_NOT_FOUND);

            // then

        }

        @DisplayName("선호조건이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void userPreferenceNotFound() {
            // given
            given(userPreferenceQuery.findByUser(any()))
                .willThrow(new GeneralException(UserException.USER_PREFERENCE_NOT_FOUND));

            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.USER_PREFERENCE_NOT_FOUND);

            // then

        }

        private static Stream<Arguments> provideBirths() {

            return Stream.of(
                Arguments.of(LocalDate.now().minusYears(19)),
                Arguments.of(LocalDate.now().minusYears(41))
            );
        }

        @DisplayName("생년월일을 바탕으로 한 나이가 20~40세 범위를 벗어날 경우 예외가 발생한다.")
        @ParameterizedTest
        @MethodSource("provideBirths")
        void ageOutOfRange(LocalDate birth) {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(birth);

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.AGE_OUT_OF_RANGE);

            // then

        }

        @DisplayName("음역대가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void vocalRangeNotFound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn("존재하지 않는 음역대");
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.VOCAL_RANGE_NOT_FOUND);

            // then

        }

        @DisplayName("선호하는 음역대가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void preferredVocalRangeNotFound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(request.getPreferredVocalRange()).willReturn("존재하지 않는 음역대");
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.VOCAL_RANGE_NOT_FOUND);

            // then

        }

        @DisplayName("닮은 동물상이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void animalNotFound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(request.getPreferredVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            given(request.getLookAlikeAnimal()).willReturn("존재하지 않는 동물상");
            given(animalQuery.findAll()).willReturn(List.of(ANIMAL));

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.ANIMAL_NOT_FOUND);

            // then

        }

        @DisplayName("선호하는 동물상이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void preferredAnimalNotFound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(request.getPreferredVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            given(request.getLookAlikeAnimal()).willReturn(ANIMAL.getName());
            given(request.getPreferredAnimal()).willReturn("존재하지 않는 동물상");
            given(animalQuery.findAll()).willReturn(List.of(ANIMAL));

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.ANIMAL_NOT_FOUND);

            // then

        }

        @DisplayName("활동 지역이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void areaNotFound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(request.getPreferredVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            given(request.getLookAlikeAnimal()).willReturn(ANIMAL.getName());
            given(request.getPreferredAnimal()).willReturn(ANIMAL.getName());
            given(animalQuery.findAll()).willReturn(List.of(ANIMAL));

            given(request.getActivityArea()).willReturn("존재하지 않는 지역");
            given(areaQuery.findAll()).willReturn(List.of(AREA));

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.AREA_NOT_FOUND);

            // then

        }

        @DisplayName("선호하는 지역이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void preferredAreaNotFound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(request.getPreferredVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            given(request.getLookAlikeAnimal()).willReturn(ANIMAL.getName());
            given(request.getPreferredAnimal()).willReturn(ANIMAL.getName());
            given(animalQuery.findAll()).willReturn(List.of(ANIMAL));

            given(request.getActivityArea()).willReturn(AREA.getName());
            given(request.getPreferredArea()).willReturn("존재하지 않는 지역");
            given(areaQuery.findAll()).willReturn(List.of(AREA));

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.AREA_NOT_FOUND);

            // then

        }

        @DisplayName("선호하는 나이 하한이 상한보다 클 경우 예외가 발생한다.")
        @Test
        void ageLowerBoundGreaterThanAgeUpperBound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(request.getPreferredVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            given(request.getLookAlikeAnimal()).willReturn(ANIMAL.getName());
            given(request.getPreferredAnimal()).willReturn(ANIMAL.getName());
            given(animalQuery.findAll()).willReturn(List.of(ANIMAL));

            given(request.getActivityArea()).willReturn(AREA.getName());
            given(request.getPreferredArea()).willReturn(AREA.getName());
            given(areaQuery.findAll()).willReturn(List.of(AREA));

            given(request.getPreferredAgeUpperBound()).willReturn(AGE_UPPER_BOUND);
            given(request.getPreferredAgeLowerBound()).willReturn(AGE_UPPER_BOUND + 1);

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND);

            // then

        }

        @DisplayName("선호하는 키 하한이 상한보다 클 경우 예외가 발생한다.")
        @Test
        void heightLowerBoundGreaterThanHeightUpperBound() {
            // given
            UserUpdateProfileRequest request = mock(UserUpdateProfileRequest.class);
            given(request.getBirth()).willReturn(BIRTH);

            given(request.getVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(request.getPreferredVocalRange()).willReturn(VOCAL_RANGE.getClassification());
            given(vocalRangeQuery.findAll()).willReturn(List.of(VOCAL_RANGE));

            given(request.getLookAlikeAnimal()).willReturn(ANIMAL.getName());
            given(request.getPreferredAnimal()).willReturn(ANIMAL.getName());
            given(animalQuery.findAll()).willReturn(List.of(ANIMAL));

            given(request.getActivityArea()).willReturn(AREA.getName());
            given(request.getPreferredArea()).willReturn(AREA.getName());
            given(areaQuery.findAll()).willReturn(List.of(AREA));

            given(request.getPreferredAgeLowerBound()).willReturn(AGE_LOWER_BOUND);
            given(request.getPreferredAgeUpperBound()).willReturn(AGE_UPPER_BOUND);

            given(request.getPreferredHeightUpperBound()).willReturn(HEIGHT_UPPER_BOUND);
            given(request.getPreferredHeightLowerBound()).willReturn(HEIGHT_UPPER_BOUND + 1);

            // when
            assertThatThrownBy(() -> myPageService.updateProfile(request, USER_ID))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.LOWER_BOUND_LESS_THAN_OR_EQUAL_UPPER_BOUND);

            // then

        }
    }

    @Nested
    @DisplayName("사용자는 프로필 사진 수정 시 ")
    class UpdateProfilePhotosTest {

        @DisplayName("수정을 성공한다.")
        @Test
        void success() {
            // given
            User user = user();
            given(userQuery.findById(USER_ID)).willReturn(user);

            MockMultipartFile profilePhoto = new MockMultipartFile("profilephotos",
                "photo.png",
                MediaType.IMAGE_JPEG_VALUE,
                "photo content".getBytes());
            UserUpdateProfilePhotoRequest request = new UserUpdateProfilePhotoRequest(
                List.of(profilePhoto, profilePhoto),
                PHOTO_SYNC_RATE
            );

            // when
            myPageService.updateProfilePhotos(USER_ID, request);

            // then
            Assertions.assertAll(
                () -> verify(s3Service).uploadProfilePhotos(anyList(), eq(user)),
                () -> assertThat(user.getPhotoSyncRate()).isEqualTo(PHOTO_SYNC_RATE)
            );
        }

        @DisplayName("사용자가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void userNotFound() {
            // given
            given(userQuery.findById(USER_ID))
                .willThrow(new GeneralException(UserException.USER_NOT_FOUND));

            UserUpdateProfilePhotoRequest request = mock(UserUpdateProfilePhotoRequest.class);

            // when
            assertThatThrownBy(() -> myPageService.updateProfilePhotos(USER_ID, request))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UserException.USER_NOT_FOUND);

            // then

        }

        @DisplayName("허용되지 않는 사진 파일 확장자일 경우 예외가 발생한다.")
        @Test
        void notAllowedImageFileExtension() {
            // given
            UserUpdateProfilePhotoRequest request = mock(UserUpdateProfilePhotoRequest.class);

            given(s3Service.uploadProfilePhotos(any(), any()))
                .willThrow(new GeneralException(UtilException.NOT_ALLOWED_IMAGE_FILE_EXTENSION));

            // when
            assertThatThrownBy(() -> myPageService.updateProfilePhotos(USER_ID, request))
                .isInstanceOf(GeneralException.class)
                .extracting("exception")
                .isEqualTo(UtilException.NOT_ALLOWED_IMAGE_FILE_EXTENSION);

            // then

        }
    }
}
