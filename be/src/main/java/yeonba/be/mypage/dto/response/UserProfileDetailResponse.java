package yeonba.be.mypage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import yeonba.be.user.entity.User;

@Getter
@AllArgsConstructor
public class UserProfileDetailResponse {

    @Schema(
        type = "array",
        description = "프로필 이미지")
    private List<String> profilePhotoUrls;

    @Schema(
        type = "string",
        description = "성별",
        example = "남")
    private String gender;

    @Schema(
        type = "string",
        description = "이름",
        example = "안민재")
    private String name;

    @Schema(
        type = "string",
        description = "생년월일",
        example = "1998-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Schema(
        type = "number",
        description = "키",
        example = "181")
    private int height;

    @Schema(
        type = "string",
        description = "이메일",
        example = "mj3242@naver.com")
    private String email;

    @Schema(
        type = "string",
        description = "전화번호",
        example = "01011112222")
    private String phoneNumber;

    @Schema(
        type = "string",
        description = "별명",
        example = "존잘남")
    private String nickname;

    @Schema(
        type = "number",
        description = "사진 싱크로율",
        example = "80")
    private double photoSyncRate;

    @Schema(
        type = "string",
        description = "체형",
        example = "메른 체형")
    private String bodyType;

    @Schema(
        type = "string",
        description = "직업",
        example = "학생")
    private String job;

    @Schema(
        type = "string",
        description = "MBTI",
        example = "ISTP")
    private String mbti;

    public UserProfileDetailResponse(User user) {

        this.profilePhotoUrls = user.getProfilePhotoUrls();
        this.gender = user.getGender();
        this.name = user.getName();
        this.birth = user.getBirth();
        this.height = user.getHeight();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.nickname = user.getNickname();
        this.photoSyncRate = user.getPhotoSyncRate();
        this.bodyType = user.getBodyType();
        this.job = user.getJob();
        this.mbti = user.getMbti();
    }
}
