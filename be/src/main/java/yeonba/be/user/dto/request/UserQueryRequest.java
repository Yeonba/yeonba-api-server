package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotBlank;
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
            - 즐겨찾는 이성 : FAVORITES
            - 나에게 관심 있는 이성(나에게 화살을 보낸 이성) : ARROW_SENDERS
            - 나에게 화살을 보낸 이성 : ARROW_RECEIVERS""",
        example = "FAVORITES",
        in = ParameterIn.QUERY)
    @NotBlank(message = "조회 기준은 반드시 입력되어야 합니다.")
    @Pattern(
        regexp = "\\b(FAVORITES|ARROW_SENDERS|ARROW_RECEIVERS)\\b",
        message = "조회 기준은 FAVORITES, ARROW_SENDERS, ARROW_RECEIVERS만 허용됩니다.")
    private String type;

    @Parameter(
        name = "page",
        description = "조회할 페이지 번호, 기본 첫 페이지(0)",
        example = "0",
        in = ParameterIn.QUERY)
    @PositiveOrZero(message = "페이지 번호는 0이상이어야 합니다.")
    private Integer page;
}
