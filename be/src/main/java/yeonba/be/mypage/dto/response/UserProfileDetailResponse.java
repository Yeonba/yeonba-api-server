package yeonba.be.mypage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;

@Getter
@AllArgsConstructor
public class UserProfileDetailResponse {

    @Schema(
        type = "array",
        description = "프로필 이미지")
    private List<String> profilePhotoUrls;

    @Schema(
        type = "string",
        description = "성별",
        example = "남")
    private String gender;

    @Schema(
        type = "string",
        description = "생년월일",
        example = "1998-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Schema(
        type = "number",
        description = "키",
        example = "181")
    private int height;

    @Schema(
        type = "string",
        description = "전화번호",
        example = "01011112222")
    private String phoneNumber;

    @Schema(
        type = "string",
        description = "별명",
        example = "존잘남")
    private String nickname;

    @Schema(
        type = "number",
        description = "사진 싱크로율",
        example = "80")
    private int photoSyncRate;

    @Schema(
        type = "string",
        description = "체형",
        example = "마른 체형")
    private String bodyType;

    @Schema(
        type = "string",
        description = "직업",
        example = "학생")
    private String job;

    @Schema(
        type = "string",
        description = "MBTI",
        example = "ISTP")
    private String mbti;

    @Schema(
        type = "string",
        description = "음역대",
        example = "고음")
    private String vocalRange;

    @Schema(
        type = "string",
        description = "닮은 동물상",
        example = "강아지상")
    private String lookAlikeAnimal;

    @Schema(
        type = "string",
        description = "활동 지역",
        example = "서울")
    private String activityArea;

    @Schema(
        type = "string",
        description = "선호하는 음역대",
        example = "고음")
    private String preferredVocalRange;

    @Schema(
        type = "string",
        description = "선호하는 동물상",
        example = "고양이상")
    private String preferredAnimal;

    @Schema(
        type = "string",
        description = "선호하는 지역",
        example = "경기")
    private String preferredArea;

    @Schema(
        type = "number",
        description = "선호하는 나이 하한, nullable",
        example = "25")
    private Integer preferredAgeLowerBound;

    @Schema(
        type = "number",
        description = "선호하는 나이 상한, nullable",
        example = "30")
    private Integer preferredAgeUpperBound;

    @Schema(
        type = "number",
        description = "선호하는 키 하한, nullable",
        example = "170")
    private Integer preferredHeightLowerBound;

    @Schema(
        type = "number",
        description = "선호하는 키 상한, nullable",
        example = "180")
    private Integer preferredHeightUpperBound;

    @Schema(
        type = "string",
        description = "선호하는 MBTI",
        example = "ISTJ")
    private String preferredMbti;

    @Schema(
        type = "string",
        description = "선호하는 체형",
        example = "마른체형")
    private String preferredBodyType;

    public static UserProfileDetailResponse from(User user, UserPreference userPreference) {

        return new UserProfileDetailResponse(
            user.getProfilePhotoUrls(),
            user.getGenderString(),
            user.getBirth(),
            user.getHeight(),
            user.getPhoneNumber(),
            user.getNickname(),
            user.getPhotoSyncRate(),
            user.getBodyType(),
            user.getJob(),
            user.getMbti(),
            user.getVocalRange().getClassification(),
            user.getAnimal().getName(),
            user.getArea().getName(),
            userPreference.getVocalRange().getClassification(),
            userPreference.getAnimal().getName(),
            userPreference.getArea().getName(),
            userPreference.getAgeLowerBound(),
            userPreference.getAgeUpperBound(),
            userPreference.getHeightLowerBound(),
            userPreference.getHeightUpperBound(),
            userPreference.getMbti(),
            userPreference.getBodyType()
        );
    }
}
