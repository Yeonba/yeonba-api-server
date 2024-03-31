package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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
            - 나에게 관심 있는 이성(나에게 화살을 보낸 이성) : ARROW_SENDERS
            - 나에게 화살을 보낸 이성 : ARROW_RECEIVERS""",
        example = "RECOMMEND",
        in = ParameterIn.QUERY)
    @NotNull(message = "조회 기준은 반드시 입력되어야 합니다.")
    @Pattern(
        regexp = "\\b(RECOMMEND|BOOKMARKED|ARROW_SENDERS|ARROW_RECEIVERS)\\b",
        message = "검색 기준은 RECOMMEND, BOOKMARKED, ARROW_SENDERS, ARROW_RECEIVERS만 허용됩니다.")
    private String type;

    @Parameter(
        name = "page",
        description = "조회할 페이지 번호, 0부터 시작",
        example = "0",
        in = ParameterIn.QUERY)
    @NotNull(message = "조회할 페이지 번호는 반드시 입력되어야 합니다.")
    @PositiveOrZero(message = "페이지 번호는 0이상이어야 합니다.")
    private int page;
}
