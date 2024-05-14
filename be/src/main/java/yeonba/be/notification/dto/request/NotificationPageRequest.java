package yeonba.be.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPageRequest {

    @Schema(
        type = "number",
        description = "페이지 번호, 기본 첫 페이지(0)",
        example = "0")
    @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
    private Integer page;
}
