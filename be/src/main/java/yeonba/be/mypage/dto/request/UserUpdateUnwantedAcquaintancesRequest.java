package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateUnwantedAcquaintancesRequest {

  @Schema(
      type = "array",
      description = "새로운 만나고 싶지 않은 지인 목록"
  )
  private List<UserUpdateUnwantedAcquaintanceRequest> newAcquaintances;
}
