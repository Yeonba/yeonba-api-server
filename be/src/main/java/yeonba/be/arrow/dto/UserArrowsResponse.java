package yeonba.be.arrow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserArrowsResponse {

  @Schema(
      type = "number",
      description = "사용자 화살 개수",
      example = "10")
  private int arrows;
}
