package yeonba.be.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockedUsersResponse {

    private List<BlockedUserResponse> blockedUsers;
}
