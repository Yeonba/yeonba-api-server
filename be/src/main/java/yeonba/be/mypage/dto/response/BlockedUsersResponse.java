package yeonba.be.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockedUsersResponse {

    @Schema(type = "array", description = "차단한 사용자 목록")
    private List<BlockedUserResponse> blockedUsers;
}
