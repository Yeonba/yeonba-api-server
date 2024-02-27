package yeonba.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserQueryResponse {

  @Schema(
      type = "string",
      description = "닉네임",
      example = "존잘남"
  )
  private String nickname;

  @Schema(
      type = "number",
      description = "받은 화살 수",
      example = "11"
  )
  private Integer receivedArrows;

  @Schema(
      type = "string",
      description = "달은 동물상",
      example = "강아지상"
  )
  private String lookAlikeAnimal;

  @Schema(
      type = "number",
      description = "사진 싱크로율",
      example = "80"
  )
  private Integer photoSyncRate;

  @Schema(
      type = "string",
      description = "활동 지역",
      example = "서울"
  )
  private String activityArea;

  @Schema(
      type = "number",
      description = "키",
      example = "180"
  )
  private Integer height;

  @Schema(
      type = "string",
      description = "음역대",
      example = "저음"
  )
  private String vocalRange;
}
