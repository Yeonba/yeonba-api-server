package yeonba.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    @Schema(
        type = "array",
        description = "프로필 사진 URL들")
    private List<String> profilePhotosUrls;

    @Schema(
        type = "string",
        description = "성별",
        example = "남")
    private String gender;

    @Schema(
        type = "string",
        description = "닉네임",
        example = "존잘남")
    private String nickname;

    @Schema(
        type = "number",
        description = "사용자가 가진 총 화살 수",
        example = "10")
    private int arrows;

    @Schema(
        type = "number",
        description = "나이",
        example = "23")
    private int age;

    @Schema(
        type = "number",
        description = "키",
        example = "177")
    private int height;

    @Schema(
        type = "string",
        description = "활동 지역",
        example = "서울")
    private String activityArea;

    @Schema(
        type = "number",
        description = "사진 싱크로율",
        example = "80")
    private int photoSyncRate;

    @Schema(
        type = "string",
        description = "음역대",
        example = "저음")
    private String vocalRange;

    @Schema(
        type = "string",
        description = "닮은 동물상",
        example = "여우상")
    private String lookAlikeAnimalName;

    @Schema(
        type = "boolean",
        description = "이전 화살 전송 여부",
        example = "false")
    private boolean isAlreadySentArrow;
}
