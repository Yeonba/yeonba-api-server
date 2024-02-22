package yeonba.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSimpleProfileResponse {

  @Schema(
      type = "string",
      description = "이름",
      example = "안민재"
  )
  private String name;
  @Schema(
      type = "string",
      description = "대표 이미지 URL",
      example = "https://yeonba-bucket.s3.ap-northeast-2.amazonaws.com/wanna_go_home.jpg"
  )
  private String profileImageUrl;
  @Schema(
      type = "number",
      description = "가진 화살 수",
      example = "10"
  )
  private Integer ownedArrows;
}
