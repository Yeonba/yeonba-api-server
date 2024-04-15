package yeonba.be.arrow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArrowSendRequest {

  @Schema(
      type = "number",
      description = "보낼 화살 수",
      example = "10")
  @Positive(message = "화살은 1개 이상부터 보낼 수 있습니다.")
  private int arrows;
}
