package yeonba.be.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {

    private String phoneNumber;
    private String name;
    private String birth;
    private String gender;
    private String email;
    private String nickname;
    private int height;
    private String activityArea;
    private int preferCelebrityId;
    private int lookAlikeCelebrityId;
    private String[] images;
    private String vocalRange;
    private int photoSyncRate;
}
