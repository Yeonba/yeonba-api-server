package yeonba.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

  @Schema(
      type = "string",
      description = "닉네임",
      example = "존잘남"
  )
  private String nickname;

  @Schema(
      type = "number",
      description = "나이",
      example = "23"
  )
  private Integer age;

  @Schema(
      type = "number",
      description = "키",
      example = "177"
  )
  private Integer height;

  @Schema(
      type = "string",
      description = "활동 지역",
      example = "서울시 강남구"
  )
  private String activityArea;

  @Schema(
      type = "number",
      description = "사진 싱크로율",
      example = "80"
  )
  private Integer photoSyncRate;

  @Schema(
      type = "string",
      description = "음역대",
      example = "저음"
  )
  private String vocalRange;

  @Schema(
      type = "string",
      description = "닮은 동물상",
      example = "여우상"
  )
  private String lookAlikeAnimalName;

  @Schema(
      type = "boolean",
      description = "이전 화살 전송 여부",
      example = "false"
  )
  private Boolean isAlreadySentArrow;
}
