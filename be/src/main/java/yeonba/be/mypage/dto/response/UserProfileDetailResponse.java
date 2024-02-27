package yeonba.be.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileDetailResponse {

  @Schema(
      type = "array",
      description = "프로필 이미지"
  )
  @Size(min = 3, max = 3)
  private List<UserProfileImageResponse> profileImages;

  @Schema(
      type = "string",
      description = "성별",
      example = "남"
  )
  private String gender;

  @Schema(
      type = "string",
      description = "이름",
      example = "안민재"
  )
  private String name;

  @Schema(
      type = "string",
      description = "생년월일",
      example = "1998-01-01"
  )
  private LocalDate birth;

  @Schema(
      type = "number",
      description = "키",
      example = "181"
  )
  private Integer height;

  @Schema(
      type = "string",
      description = "이메일",
      example = "mj3242@naver.com"
  )
  private String email;

  @Schema(
      type = "string",
      description = "전화번호",
      example = "01011112222"
  )
  private String phoneNumber;

  @Schema(
      type = "string",
      description = "별명",
      example = "존잘남"
  )
  private String nickname;

  @Schema(
      type = "number",
      description = "사진 싱크로율",
      example = "80"
  )
  private Integer photoSyncRate;

  @Schema(
      type = "string",
      description = "체형",
      example = "메른 체형"
  )
  private String bodyType;

  @Schema(
      type = "string",
      description = "직업",
      example = "학생"
  )
  private String job;

  @Schema(
      type = "string",
      description = "음주 성향",
      example = "자주"
  )
  private String drinkingTendency;

  @Schema(
      type = "string",
      description = "흡연 성향",
      example = "가끔"
  )
  private String smokingTendency;

  @Schema(
      type = "string",
      description = "MBTI",
      example = "ISTP"
  )
  private String mbti;

  @Schema(
      type = "string",
      description = "가입 일시, yyyy-MM-dd HH:mm:ss 포맷",
      example = "2012-10-01 11:30:02"
  )
  private LocalDateTime joinedAt;
}
