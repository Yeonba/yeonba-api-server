package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateProfileRequest {

    @Schema(
        type = "string",
        description = "별명",
        example = "재벌3세"
    )
    @NotBlank(message = "별명은 필수입니다.")
    private String nickname;

    @Schema(
        type = "number",
        description = "키",
        example = "177"
    )
    @NotNull(message = "키는 필수입니다.")
    private int height;

    @Schema(
        type = "string",
        description = "체형",
        example = "조금 통통"
    )
    @NotBlank(message = "체형은 필수입니다.")
    private String bodyType;

    @Schema(
        type = "string",
        description = "직업",
        example = "직장인"
    )
    @NotBlank(message = "직업은 필수입니다.")
    private String job;

    @Schema(
        type = "string",
        description = "활동 지역",
        example = "서울시 강남구"
    )
    @NotBlank(message = "활동 지역은 필수입니다.")
    private String activityArea;

    @Schema(
        type = "string",
        description = "음주 성향",
        example = "가끔"
    )
    @NotBlank(message = "음주 성향은 필수입니다.")
    private String drinkingTendency;

    @Schema(
        type = "string",
        description = "흡연 성향",
        example = "자주"
    )
    @NotBlank(message = "흡연 성향은 필수입니다.")
    private String smokingTendency;

    @Schema(
        type = "string",
        description = "음역대",
        example = "고음"
    )
    @NotBlank(message = "음역대는 필수입니다.")
    private String vocalRange;

    @Schema(
        type = "string",
        description = "MBTI",
        example = "INFP"
    )
    private String mbti;

    @Schema(
        type = "string",
        description = "선호하는 닮은 동물상",
        example = "고양이상"
    )
    @NotBlank(message = "선호하는 닮은 동물상은 필수입니다.")
    private String preferredAnimalAppearance;

    @Schema(
        type = "string",
        description = "선호하는 지역",
        example = "서울시 마포구"
    )
    @NotBlank(message = "선호하는 지역은 필수입니다.")
    private String preferredArea;

    @Schema(
        type = "string",
        description = "선호하는 음역대",
        example = "저음"
    )
    @NotBlank(message = "선호하는 음역대는 필수입니다.")
    private String preferredVocalRange;

    @Schema(
        type = "number",
        description = "선호하는 나이 하한",
        example = "21"
    )
    @Min(value = 0, message = "선호하는 나이 하한은 0 이상이어야 합니다.")
    private int preferredAgeLowerBound;

    @Schema(
        type = "number",
        description = "선호하는 나이 상한",
        example = "30"
    )
    @Min(value = 0, message = "선호하는 나이 상한은 0 이상이어야 합니다.")
    private int preferredAgeUpperBound;

    @Schema(
        type = "string",
        description = "선호하는 체형",
        example = "마른 체형"
    )
    @NotBlank(message = "선호하는 체형은 필수입니다.")
    private String preferredBodyType;
}
