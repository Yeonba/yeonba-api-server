package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDormantRequest {

    @Schema(description = "휴면 계정 상태", example = "true")
    @NotNull(message = "휴면 계정 상태는 필수 입력 값입니다.")
    private boolean status;
}
