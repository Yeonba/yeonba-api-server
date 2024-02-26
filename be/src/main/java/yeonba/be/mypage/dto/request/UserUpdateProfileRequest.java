package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
  private String nickname;

  @Schema(
      type = "number",
      description = "키",
      example = "177"
  )
  private Integer height;

  @Schema(
      type = "string",
      description = "체형",
      example = "조금 통통"
  )
  private String bodyType;

  @Schema(
      type = "string",
      description = "직업",
      example = "직장인"
  )
  private String job;

  @Schema(
      type = "string",
      description = "활동 지역",
      example = "서울시 강남구"
  )
  private String activityArea;

  @Schema(
      type = "string",
      description = "음주 성향",
      example = "가끔"
  )
  private String drinkingTendency;
  @Schema(
      type = "string",
      description = "흡연 성향",
      example = "자주"
  )
  private String smokingTendency;

  @Schema(
      type = "string",
      description = "음역대",
      example = "고음"
  )
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
  private String preferredAnimalAppearance;

  @Schema(
      type = "string",
      description = "선호하는 지역",
      example = "서울시 마포구"
  )
  private String preferredArea;

  @Schema(
      type = "string",
      description = "선호하는 음역대",
      example = "저음"
  )
  private String preferredVocalRange;

  @Schema(
      type = "number",
      description = "선호하는 나이 하한",
      example = "21"
  )
  private Integer preferredAgeLowerBound;

  @Schema(
      type = "number",
      description = "선호하는 나이 상한",
      example = "30"
  )
  private Integer preferredAgeUpperBound;

  @Schema(
      type = "string",
      description = "선호하는 체형",
      example = "마른 체형"
  )
  private String preferredBodyType;
}
