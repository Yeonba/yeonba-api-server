package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {

    @Schema(description = "휴대폰 번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "이름", example = "안민재")
    private String name;

    @Schema(description = "생일", example = "980315")
    private String birth;

    @Schema(description = "성별", example = "남자")
    private String gender;

    @Schema(description = "이메일", example = "anminjae@gmail.com")
    private String email;

    @Schema(description = "닉네임", example = "calm_min")
    private String nickname;

    @Schema(description = "키", example = "160")
    private int height;

    @Schema(description = "활동 지역", example = "서울시 성북구")
    private String activityArea;

    @Schema(description = "선호하는 동물상 ID", example = "1")
    private int preferAnimalId;

    @Schema(description = "닮은 동물상 ID", example = "1")
    private int lookAlikeAnimalId;

    @Schema(description = "프로필 이미지", example = "https://avatars.githubusercontent.com/u/156646513?s=200&v=4")
    private String[] images;

    @Schema(description = "목소리", example = "중음")
    private String vocalRange;

    @Schema(description = "사진 싱그로율", example = "60")
    private int photoSyncRate;
}
