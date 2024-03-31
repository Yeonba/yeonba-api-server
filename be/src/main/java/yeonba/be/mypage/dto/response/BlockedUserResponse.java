package yeonba.be.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import yeonba.be.user.entity.User;

@Getter
@AllArgsConstructor
public class BlockedUserResponse {

    @Schema(
        type = "number",
        description = "사용자 ID",
        example = "1")
    private long id;

    @Schema(
        type = "string",
        description = "프로필 사진 URL",
        example = "https://yeonba.com/profile/1")
    private String profileUrl;

    @Schema(
        type = "string",
        description = "사용자 이름",
        example = "안민재")
    private String name;

    public BlockedUserResponse(User blockedUser) {

        this.id = blockedUser.getId();
        this.profileUrl = blockedUser.getRepresentativeProfilePhoto();
        this.name = blockedUser.getName();
    }
}
