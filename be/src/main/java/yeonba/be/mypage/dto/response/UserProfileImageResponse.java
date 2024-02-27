package yeonba.be.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileImageResponse {

  @Schema(
      type = "number",
      description = "사용자 프로필 이미지 ID",
      example = "1"
  )
  private Long id;

  @Schema(
      type = "string",
      description = "사진 이미지 URL",
      example = "https://yeonba-bucket.s3.ap-northeast-2.amazonaws.com/wanna_go_home.jpg"
  )
  private String photoUrl;

  @Schema(
      type = "string",
      description = "등록 일시",
      example = "2012-10-01 12:00:05"
  )
  private LocalDateTime registeredAt;
}
