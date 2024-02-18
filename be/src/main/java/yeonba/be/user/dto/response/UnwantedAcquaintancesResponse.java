package yeonba.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnwantedAcquaintancesResponse {

    @Schema(description = "만나고 싶지 않은 지인 목록")
    private List<UnwantedAcquaintanceResponse> unwantedAcquaintances;
}
