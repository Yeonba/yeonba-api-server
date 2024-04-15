package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserQueryRequest {

  @Parameter(
      name = "type",
      description = """
          조회 기준
          - 추천 이성(선호 조건 바탕) : RECOMMEND
          - 즐겨찾는 이성 : BOOKMARKED
          - 나에게 관심 있는 이성(나에게 화살을 보낸 이성) : RECEIVED_ARROWS
          - 나에게 화살을 보낸 이성 : SENT_ARROWS
          - 검색 : SEARCH
          """,
      example = "RECOMMEND",
      in = ParameterIn.QUERY
  )
  @NotNull
  private String type;

  @Parameter(
      name = "page",
      description = "조회할 페이지 번호, 0부터 시작",
      example = "0",
      in = ParameterIn.QUERY
  )
  @NotNull
  @PositiveOrZero
  private Integer page;

  @Parameter(
      name = "size",
      description = "조회할 데이터 수(페이지 사이즈)",
      example = "5",
      in = ParameterIn.QUERY
  )
  @Positive
  private Integer size;

  @Parameter(
      name = "area",
      description = "활동 지역, 사용자 검색시 사용",
      example = "서울",
      in = ParameterIn.QUERY
  )
  private String area;

  @Parameter(
      name = "vocalRange",
      description = "음역대, 사용자 검색시 사용",
      example = "저음",
      in = ParameterIn.QUERY
  )
  private String vocalRange;

  @Parameter(
      name = "age",
      description = "나이 범위(하한,상한), 사용자 검색시 사용",
      example = "20,25",
      in = ParameterIn.QUERY,
      explode = Explode.FALSE
  )
  private List<Integer> ages;

  @Parameter(
      name = "height",
      description = "키 범위(하한,상한), 사용자 검색시 사용",
      example = "160,180",
      in = ParameterIn.QUERY,
      explode = Explode.FALSE
  )
  private List<Integer> heights;

  @Parameter(
      name = "includePreferredAnimal",
      description = "선호하는 동물상 포함 검색 여부, 사용자 검색시 사용",
      example = "true",
      in = ParameterIn.QUERY
  )
  private Boolean includePreferredAnimal;
}
