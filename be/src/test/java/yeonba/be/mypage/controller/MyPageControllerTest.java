package yeonba.be.mypage.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static yeonba.be.fixtures.UserFixtures.AGE_LOWER_BOUND;
import static yeonba.be.fixtures.UserFixtures.AGE_UPPER_BOUND;
import static yeonba.be.fixtures.UserFixtures.ANIMAL;
import static yeonba.be.fixtures.UserFixtures.AREA;
import static yeonba.be.fixtures.UserFixtures.ARROW;
import static yeonba.be.fixtures.UserFixtures.BIRTH;
import static yeonba.be.fixtures.UserFixtures.BODY_TYPE;
import static yeonba.be.fixtures.UserFixtures.GENDER;
import static yeonba.be.fixtures.UserFixtures.HEIGHT;
import static yeonba.be.fixtures.UserFixtures.HEIGHT_LOWER_BOUND;
import static yeonba.be.fixtures.UserFixtures.HEIGHT_UPPER_BOUND;
import static yeonba.be.fixtures.UserFixtures.JOB;
import static yeonba.be.fixtures.UserFixtures.MBTI;
import static yeonba.be.fixtures.UserFixtures.NICKNAME;
import static yeonba.be.fixtures.UserFixtures.PHONE_NUMBER;
import static yeonba.be.fixtures.UserFixtures.PHOTO_SYNC_RATE;
import static yeonba.be.fixtures.UserFixtures.PROFILE_PHOTO_URL;
import static yeonba.be.fixtures.UserFixtures.PROFILE_PHOTO_URLS;
import static yeonba.be.fixtures.UserFixtures.USER_ID;
import static yeonba.be.fixtures.UserFixtures.VOCAL_RANGE;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import yeonba.be.mypage.dto.request.UserUpdateProfilePhotoRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.mypage.service.MyPageService;
import yeonba.be.support.ControllerTestSupport;

class MyPageControllerTest extends ControllerTestSupport {

    @Mock
    private MyPageService myPageService;

    @InjectMocks
    private MyPageController myPageController;

    @Override
    protected Object initController() {

        return myPageController;
    }

    @DisplayName("사용자는 자신의 프로필을 조회할 수 있다.")
    @Test
    void getSimpleProfile() throws Exception {
        // given
        UserSimpleProfileResponse response = new UserSimpleProfileResponse(
            NICKNAME,
            PROFILE_PHOTO_URL,
            ARROW
        );
        given(myPageService.getSimpleProfile(USER_ID)).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(get("/users/profiles")
            .requestAttr("userId", USER_ID));

        // then
        assertOk(resultActions)
            .andExpect(jsonPath("$.data.name").value(response.getName()))
            .andExpect(jsonPath("$.data.profileImageUrl").value(response.getProfileImageUrl()))
            .andExpect(jsonPath("$.data.arrows").value(response.getArrows()));
    }

    @DisplayName("사용자는 자신의 상세 프로필을 조회할 수 있다.")
    @Test
    void getProfileDetail() throws Exception {
        // given
        UserProfileDetailResponse response = new UserProfileDetailResponse(
            PROFILE_PHOTO_URLS,
            GENDER.genderString,
            BIRTH,
            HEIGHT,
            PHONE_NUMBER,
            NICKNAME,
            PHOTO_SYNC_RATE,
            BODY_TYPE,
            JOB,
            MBTI,
            VOCAL_RANGE.getClassification(),
            ANIMAL.getName(),
            AREA.getName(),
            VOCAL_RANGE.getClassification(),
            ANIMAL.getName(),
            AREA.getName(),
            AGE_LOWER_BOUND,
            AGE_UPPER_BOUND,
            HEIGHT_LOWER_BOUND,
            HEIGHT_UPPER_BOUND,
            MBTI,
            BODY_TYPE
        );
        given(myPageService.getProfileDetail(USER_ID)).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(get("/users/profiles/details")
            .requestAttr("userId", USER_ID));

        // then
        assertOk(resultActions)
            .andExpect(jsonPath("$.data.profilePhotoUrls.length()")
                .value(response.getProfilePhotoUrls().size()))
            .andExpect(jsonPath("$.data.profilePhotoUrls[0]")
                .value(response.getProfilePhotoUrls().get(0)))
            .andExpect(jsonPath("$.data.profilePhotoUrls[1]")
                .value(response.getProfilePhotoUrls().get(1)))
            .andExpect(jsonPath("$.data.gender").value(response.getGender()))
            .andExpect(jsonPath("$.data.birth").value(response.getBirth().toString()))
            .andExpect(jsonPath("$.data.height").value(response.getHeight()))
            .andExpect(jsonPath("$.data.phoneNumber").value(response.getPhoneNumber()))
            .andExpect(jsonPath("$.data.nickname").value(response.getNickname()))
            .andExpect(jsonPath("$.data.photoSyncRate").value(response.getPhotoSyncRate()))
            .andExpect(jsonPath("$.data.bodyType").value(response.getBodyType()))
            .andExpect(jsonPath("$.data.job").value(response.getJob()))
            .andExpect(jsonPath("$.data.mbti").value(response.getMbti()))
            .andExpect(jsonPath("$.data.vocalRange").value(response.getVocalRange()))
            .andExpect(jsonPath("$.data.lookAlikeAnimal").value(response.getLookAlikeAnimal()))
            .andExpect(jsonPath("$.data.activityArea").value(response.getActivityArea()))
            .andExpect(jsonPath("$.data.preferredVocalRange")
                .value(response.getPreferredVocalRange()))
            .andExpect(jsonPath("$.data.preferredAnimal").value(response.getPreferredAnimal()))
            .andExpect(jsonPath("$.data.preferredArea").value(response.getPreferredArea()))
            .andExpect(jsonPath("$.data.preferredAgeLowerBound")
                .value(response.getPreferredAgeLowerBound()))
            .andExpect(jsonPath("$.data.preferredAgeUpperBound")
                .value(response.getPreferredAgeUpperBound()))
            .andExpect(jsonPath("$.data.preferredHeightLowerBound")
                .value(response.getPreferredHeightLowerBound()))
            .andExpect(jsonPath("$.data.preferredHeightUpperBound")
                .value(response.getPreferredHeightUpperBound()))
            .andExpect(jsonPath("$.data.preferredMbti").value(response.getPreferredMbti()))
            .andExpect(jsonPath("$.data.preferredBodyType").value(response.getPreferredBodyType()));
    }

    @Nested
    @DisplayName("사용자는 자신의 프로필 사진 수정 시 ")
    class UpdateProfilePhotosTest {

        private static final MockMultipartFile profilePhoto = new MockMultipartFile("profilePhotos",
            "photo.png",
            MediaType.IMAGE_PNG_VALUE,
            "photo content".getBytes());

        @DisplayName("수정을 성공할 수 있다.")
        @Test
        void success() throws Exception {
            // given
            doNothing().when(myPageService)
                .updateProfilePhotos(eq(USER_ID), any(UserUpdateProfilePhotoRequest.class));

            // when
            ResultActions resultActions = mockMvc.perform(
                multipart(HttpMethod.PUT, "/users/profile-photos")
                    .file(profilePhoto)
                    .file(profilePhoto)
                    .param("photoSyncRate", String.valueOf(PHOTO_SYNC_RATE))
                    .requestAttr("userId", USER_ID)
            );

            // then
            assertOk(resultActions)
                .andExpect(jsonPath("$.data").isEmpty());
        }

        @DisplayName("사진은 필수값이다.")
        @Test
        void withoutProfilePhotos() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                multipart(HttpMethod.PUT, "/users/profile-photos")
                    .param("photoSyncRate", String.valueOf(PHOTO_SYNC_RATE))
                    .requestAttr("userId", USER_ID)
            );

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.profilePhotos").value("프로필 사진은 반드시 입력되어야 합니다."));
        }

        private static Stream<Arguments> provideProfilePhotos() {

            return Stream.of(
                Arguments.of(List.of(profilePhoto)),
                Arguments.of(List.of(profilePhoto, profilePhoto, profilePhoto))
            );
        }

        @DisplayName("사진은 정확히 2장이어야 한다.")
        @ParameterizedTest
        @MethodSource("provideProfilePhotos")
        void invalidNumberOfProfilePhotos(List<MockMultipartFile> profilePhotos) throws Exception {
            // given
            MockMultipartHttpServletRequestBuilder requestBuilder = multipart(HttpMethod.PUT,
                "/users/profile-photos");
            profilePhotos.forEach(requestBuilder::file);

            // when
            ResultActions resultActions = mockMvc.perform(
                requestBuilder.param("photoSyncRate", String.valueOf(PHOTO_SYNC_RATE))
                    .requestAttr("userId", USER_ID)
            );

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.profilePhotos").value("프로필 사진은 정확히 2장이어야 합니다."));
        }

        @DisplayName("사진 싱크로율은 75~100 범위내 값만 가능하다.")
        @ParameterizedTest
        @ValueSource(ints = {74, 101})
        void photoSyncRateOutOfRange(int photoSyncRate) throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                multipart(HttpMethod.PUT, "/users/profile-photos")
                    .file(profilePhoto)
                    .file(profilePhoto)
                    .param("photoSyncRate", String.valueOf(photoSyncRate))
                    .requestAttr("userId", USER_ID)
            );

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.photoSyncRate").value("사진 싱크로율은 75~100 범위내 값만 가능합니다."));
        }
    }

    @Nested
    @DisplayName("사용자는 자신의 프로필 수정 시 ")
    class UpdateProfileTest {

        private MockHttpServletRequestBuilder buildUpdateProfileRequest(
            UserUpdateProfileRequest request) throws Exception {

            return patch("/users/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .requestAttr("userId", USER_ID);
        }

        @DisplayName("수정에 성공한다.")
        @Test
        void success() throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertOk(resultActions)
                .andExpect(jsonPath("$.data").isEmpty());
        }

        @DisplayName("닉네임은 필수값이며 공백일 수 없다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void emptyNickname(String nickname) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                nickname,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.nickname")
                    .value(Matchers.containsString("닉네임은 반드시 입력되어야 합니다.")));
        }

        @DisplayName("닉네임은 공백 없이 영어 대소문자,한글,숫자로 구성되어야 한다.")
        @ParameterizedTest
        @ValueSource(strings = {"!!!!***", "공백존재 닉네임"})
        void invalidNickname(String nickname) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                nickname,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.nickname")
                    .value("닉네임은 공백 없이 영어 대소문자,한글,숫자로 구성되어야 하며 최대 8자까지 가능합니다."));
        }

        @DisplayName("닉네임은 8자를 초과할 수 없다.")
        @Test
        void nicknameOverMaxLength() throws Exception {
            // given
            String nickname = "8자를초과하는닉네임";
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                nickname,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.nickname").value(
                    "닉네임은 공백 없이 영어 대소문자,한글,숫자로 구성되어야 하며 최대 8자까지 가능합니다."));
        }

        @DisplayName("키는 130~220cm 내 값만 가능하다.")
        @ParameterizedTest
        @ValueSource(ints = {129, 221})
        void heightOutOfRange(int height) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                height,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.height").value("키는 130 ~ 220cm 내 값만 가능합니다."));
        }

        @DisplayName("음역대는 필수값이며 공백일 수 없다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void emptyVocalRange(String vocalRange) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                vocalRange,
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.vocalRange").value("음역대는 반드시 입력되어야 합니다."));
        }

        @DisplayName("생년월일은 필수값이다.")
        @Test
        void withoutBirth() throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                null,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.birth").value("생년월일은 반드시 입력되어야 합니다."));
        }

        @DisplayName("생년월일은 미래일 수 없다.")
        @Test
        void futureBirth() throws Exception {
            // given
            LocalDate birth = LocalDate.now().plusYears(1);
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                birth,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.birth").value("생년월일은 현재 날짜와 같거나 과거여야 합니다."));
        }

        @DisplayName("체형은 필수값이며 공백일 수 없다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void emptyBodyType(String bodyType) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                bodyType,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.bodyType").value("체형은 반드시 입력되어야 합니다."));
        }

        @DisplayName("직업은 필수값이며 공백일 수 없다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void emptyJob(String job) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                job,
                AREA.getName(),
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.job").value("직업은 반드시 입력되어야 합니다."));
        }

        @DisplayName("활동 지역은 필수값이며 공백일 수 없다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void emptyActivityArea(String activityArea) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                activityArea,
                ANIMAL.getName(),
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.activityArea").value("활동 지역은 반드시 입력되어야 합니다."));
        }

        @DisplayName("닮은 동물상은 필수값이며 공백일 수 없다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void emptyLookAlikeAnimal(String lookAlikeAnimal) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                lookAlikeAnimal,
                MBTI,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.lookAlikeAnimal").value("닮은 동물상은 반드시 입력되어야 합니다."));
        }

        @DisplayName("MBTI는 필수값이며 공백일 수 없다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void emptyMbti(String mbti) throws Exception {
            // given
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                mbti,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.mbti")
                    .value(Matchers.containsString("MBTI는 반드시 입력되어야 합니다.")));
        }

        @DisplayName("MBTI는 MBTI 형식에 맞아야 한다.")
        @Test
        void invalidMbti() throws Exception {
            // given
            String invalidMbti = "ABCD";
            UserUpdateProfileRequest request = new UserUpdateProfileRequest(
                NICKNAME,
                HEIGHT,
                VOCAL_RANGE.getClassification(),
                BIRTH,
                BODY_TYPE,
                JOB,
                AREA.getName(),
                ANIMAL.getName(),
                invalidMbti,
                ANIMAL.getName(),
                AREA.getName(),
                VOCAL_RANGE.getClassification(),
                AGE_LOWER_BOUND,
                AGE_UPPER_BOUND,
                HEIGHT_LOWER_BOUND,
                HEIGHT_UPPER_BOUND,
                BODY_TYPE,
                MBTI
            );

            // when
            ResultActions resultActions = mockMvc.perform(buildUpdateProfileRequest(request));

            // then
            assertBadRequest(resultActions)
                .andExpect(jsonPath("$.data.mbti").value("유효하지 않은 MBTI 형식입니다."));
        }
    }
}