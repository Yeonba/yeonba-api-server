package yeonba.be.mypage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockedUserResponse {

    private long id;
    private String profileUrl;
    private String name;
}
