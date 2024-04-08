package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateUnwantedAcquaintancesRequest {

    @Schema(type = "array", description = "새로운 만나고 싶지 않은 지인 목록")
    @NotNull(message = "만나고 싶지 않은 지인 목록은 필수입니다.")
    private List<UserUpdateUnwantedAcquaintanceRequest> acquaintances;
}
