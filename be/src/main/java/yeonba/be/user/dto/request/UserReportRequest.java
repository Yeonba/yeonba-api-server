package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportRequest {

  @Schema(
      type = "string",
      description = "신고 분류",
      example = "부적절한 프로필 이미지"
  )
  private String category;

  @Schema(
      type = "string",
      description = "신고 사유, 분류가 기타일 경우에만 작성",
      example = "null"
  )
  private String reason;
}
