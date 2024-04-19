package yeonba.be.notification.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationReceivedRequest {

    @Parameter(
        name = "page",
        description = "페이지 번호",
        example = "0",
        in = ParameterIn.QUERY)
    @NotNull(message = "페이지 번호는 반드시 포함되어야 합니다.")
    @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
    private Integer page;
}
