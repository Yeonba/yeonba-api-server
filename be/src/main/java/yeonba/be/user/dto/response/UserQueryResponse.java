package yeonba.be.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserQueryResponse {

    @Schema(
        type = "number",
        description = "사용자 ID",
        example = "12")
    private long id;

    @Schema(
        type = "string",
        description = "대표 프로필 사진 URL",
        example = "profilephoto/2-1")
    private String profilePhotoUrl;

    @Schema(
        type = "string",
        description = "닉네임",
        example = "존잘남")
    private String nickname;

    @Schema(
        type = "string",
        description = "나이",
        example = "22")
    private int age;

    @Schema(
        type = "number",
        description = "총 받은 화살 수",
        example = "11")
    private int receivedArrows;

    @Schema(
        type = "string",
        description = "달은 동물상",
        example = "강아지상")
    private String lookAlikeAnimal;

    @Schema(
        type = "number",
        description = "사진 싱크로율",
        example = "80")
    private int photoSyncRate;

    @Schema(
        type = "string",
        description = "활동 지역",
        example = "서울")
    private String activityArea;

    @Schema(
        type = "number",
        description = "키",
        example = "180")
    private int height;

    @Schema(
        type = "string",
        description = "음역대",
        example = "저음")
    private String vocalRange;

    @Schema(
        type = "boolean",
        description = "즐겨찾기 여부",
        example = "false")
    private Boolean isFavorite;

    // TODO : 채팅 가능 여부 추가
}
