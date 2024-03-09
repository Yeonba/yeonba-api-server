package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class UserReportRequest {

  @Schema(
      type = "string",
      description = "신고 분류",
      example = "부적절한 프로필 이미지")
  @NotBlank
  @Length(max = 255, message = "카테고리 길이는 255자를 넘을 수 없습니다.")
  private String category;

  @Schema(
      type = "string",
      description = "신고 사유, 분류가 기타일 경우에만 작성",
      example = "null")
  private String reason;
}
