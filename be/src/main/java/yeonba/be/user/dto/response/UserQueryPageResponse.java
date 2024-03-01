package yeonba.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserQueryPageResponse {

  @Schema(
      type = "array",
      description = "조회된 이성(사용자) 목록"
  )
  @Size(min = 3)
  private List<UserQueryResponse> users;

  @Schema(
      type = "number",
      description = "조회된 전체 페이지 수",
      example = "10"
  )
  private Integer totalPage;

  @Schema(
      type = "number",
      description = "조회된 전체 데이터 수",
      example = "1000"
  )
  private Long totalElements;

  @Schema(
      type = "boolean",
      description = "첫 페이지 여부",
      example = "true"
  )
  private Boolean isFirstPage;

  @Schema(
      type = "boolean",
      description = "마지막 페이지 여부",
      example = "false"
  )
  private Boolean isLastPage;
}
