package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
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
        example = "재벌3세")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣]{1,8}$",
        message = "닉네임은 공백 없이 영어 대소문자,한글,숫자로 구성되어야 하며 최대 8자까지 가능합니다.")
    @NotBlank(message = "닉네임은 반드시 입력되어야 합니다.")
    private String nickname;

    @Schema(
        type = "number",
        description = "키",
        example = "177")
    @Positive(message = "키는 양수여야 합니다.")
    @NotNull(message = "키는 반드시 입력되어야 합니다.")
    private int height;

    @Schema(
        type = "string",
        description = "음역대",
        example = "고음")
    @NotBlank(message = "음역대는 반드시 입력되어야 합니다.")
    private String vocalRange;

    @Schema(
        type = "string",
        description = "생년월일",
        example = "1998-04-08")
    @PastOrPresent(message = "생년월일은 현재 날짜와 같거나 과거여야 합니다.")
    @NotNull(message = "생년월일은 반드시 입력되어야 합니다.")
    private LocalDate birth;

    @Schema(
        type = "string",
        description = "체형",
        example = "조금 통통")
    @NotBlank(message = "체형은 반드시 입력되어야 합니다.")
    private String bodyType;

    @Schema(
        type = "string",
        description = "직업",
        example = "직장인")
    @NotBlank(message = "직업은 반드시 입력되어야 합니다.")
    private String job;

    @Schema(
        type = "string",
        description = "활동 지역",
        example = "서울")
    @NotBlank(message = "활동 지역은 반드시 입력되어야 합니다.")
    private String activityArea;

    @Schema(
        type = "string",
        description = "닮은 동물상",
        example = "강아지상")
    @NotBlank(message = "닮은 동물상은 반드시 입력되어야 합니다.")
    private String lookAlikeAnimal;

    @Schema(
        type = "string",
        description = "MBTI",
        example = "INFP")
    @Pattern(
        regexp = "^[EI][SN][TF][JP]$",
        message = "유효하지 않은 MBTI 형식입니다.")
    @NotBlank(message = "MBTI는 반드시 입력되어야 합니다.")
    private String mbti;

    @Schema(
        type = "string",
        description = "선호하는 닮은 동물상",
        example = "고양이상")
    @NotBlank(message = "선호하는 닮은 동물상은 반드시 입력되어야 합니다.")
    private String preferredAnimal;

    @Schema(
        type = "string",
        description = "선호하는 지역",
        example = "서울")
    @NotBlank(message = "선호하는 지역은 반드시 입력되어야 합니다.")
    private String preferredArea;

    @Schema(
        type = "string",
        description = "선호하는 음역대",
        example = "저음")
    @NotBlank(message = "선호하는 음역대는 반드시 입력되어야 합니다.")
    private String preferredVocalRange;

    @Schema(
        type = "number",
        description = "선호하는 나이 하한",
        example = "21")
    @PositiveOrZero(message = "선호하는 나이 하한은 0 이상이어야 합니다.")
    private Integer preferredAgeLowerBound;

    @Schema(
        type = "number",
        description = "선호하는 나이 상한",
        example = "30")
    @PositiveOrZero(message = "선호하는 나이 상한은 0 이상이어야 합니다.")
    private Integer preferredAgeUpperBound;

    @Schema(
        type = "number",
        description = "선호하는 키 하한",
        example = "177")
    @PositiveOrZero(message = "선호하는 키 하한은 0 이상이어야 합니다.")
    private Integer preferredHeightLowerBound;

    @Schema(
        type = "number",
        description = "선호하는 키 상한",
        example = "185")
    @PositiveOrZero(message = "선호하는 키 상한은 0 이상이어야 합니다.")
    private Integer preferredHeightUpperBound;

    @Schema(
        type = "string",
        description = "선호하는 체형",
        example = "마른 체형")
    @NotBlank(message = "선호하는 체형은 반드시 입력되어야 합니다.")
    private String preferredBodyType;

    @Schema(
        type = "string",
        description = "선호하는 MBTI",
        example = "ISTJ")
    @Pattern(
        regexp = "^[EI][SN][TF][JP]$",
        message = "유효하지 않은 MBTI 형식입니다.")
    @NotBlank(message = "선호하는 MBTI는 반드시 입력되어야 합니다.")
    private String preferredMbti;
}
