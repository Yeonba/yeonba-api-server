package yeonba.be.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yeonba.be.user.entity.User;

@Getter
@AllArgsConstructor
public class BlockedUserResponse {

    private long id;
    private String profileUrl;
    private String name;

    public BlockedUserResponse(User blockedUser) {

        this.id = blockedUser.getId();
        this.profileUrl = blockedUser.getRepresentativeProfilePhoto();
        this.name = blockedUser.getName();
    }
}
