package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UserJoinRequest {

    @Schema(
        type = "string",
        description = "성별",
        example = "남")
    @Pattern(
        regexp = "^(남|여)$",
        message = "성별은 남 또는 여만 가능합니다.")
    @NotBlank(message = "성별은 반드시 입력되어야 합니다.")
    private String gender;

    @Schema(
        type = "string",
        description = "전화번호",
        example = "01011112222")
    @Pattern(
        regexp = "^010\\d{8}$",
        message = "전화번호는 11자리 010으로 시작하며 하이픈(-) 없이 0~9의 숫자로 이뤄져야 합니다.")
    @NotBlank(message = "전화번호는 반드시 입력되어야 합니다.")
    private String phoneNumber;

    @Schema(
        type = "string",
        description = "비밀번호",
        example = "Aa1234!@")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$",
        message = """
            비밀번호는 영어대소문자, 숫자, 특수문자(~#@!)를
            최소 1자씩 포함하며 8~20자 사이여야 합니다.""")
    @NotBlank(message = "비밀번호는 반드시 입력되어야 합니다.")
    private String password;

    @Schema(
        type = "string",
        description = "비밀번호 확인값",
        example = "Aa1234!@")
    @NotBlank(message = "비밀번호 확인값은 반드시 입력되어야 합니다.")
    private String passwordConfirmation;

    @Schema(
        type = "string",
        description = "이메일",
        example = "mj3242@naver.com")
    @Pattern(
        regexp = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
        message = "유효하지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일은 반드시 입력되어야 합니다.")
    private String email;

    @Schema(
        type = "string",
        description = "생년월일",
        example = "1998-04-08")
    @NotNull(message = "생년월일은 반드시 입력되어야 합니다.")
    private LocalDate birth;

    @Schema(
        type = "string",
        description = "이름",
        example = "안민재")
    @NotBlank(message = " 이름은 반드시 입력되어야 합니다.")
    private String name;

    @Schema(
        type = "string",
        description = "닉네임",
        example = "존잘남")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣]{1,8}$",
        message = "닉네임은 공백 없이 영어 대소문자,한글,숫자로 구성되어야 하며 최대 8자까지 가능합니다.")
    @NotBlank(message = "닉네임은 반드시 입력되어야 합니다.")
    private String nickname;

    @Schema(
        type = "number",
        description = "키",
        example = "180")
    @Positive(message = "키는 양의 정수여야 합니다.")
    private int height;

    @Schema(
        type = "string",
        description = "체형",
        example = "마른체형")
    @NotBlank(message = "체형은 반드시 입력되어야 합니다.")
    private String bodyType;

    @Schema(
        type = "string",
        description = "직업",
        example = "학생")
    @NotBlank(message = "직업은 반드시 입력되어야 합니다.")
    private String job;

    @Schema(
        type = "string",
        description = "활동 지역",
        example = "서울")
    @NotBlank(message = "활동 지역은 반드시 입력되어야 합니다.")
    private String activityArea;

    @Schema(
        type = "string",
        description = "MBTI",
        example = "ESTJ")
    @Pattern(
        regexp = "^[EI][SN][TF][JP]$",
        message = "유효하지 않은 MBTI 형식입니다.")
    @NotBlank(message = "MBTI는 반드시 입력되어야 합니다.")
    private String mbti;

    @Schema(
        type = "string",
        description = "음역대",
        example = "저음")
    @NotBlank(message = "음역대는 반드시 입력되어야 합니다.")
    private String vocalRange;

    @Schema(
        type = "array",
        description = "프로필 사진 파일들")
    @Size(min = 2, max = 2)
    private List<MultipartFile> profilePhotos;

    @Schema(
        type = "number",
        description = "사진 싱크로율",
        example = "80")
    @Min(
        value = 80,
        message = "사진 싱크로율이 80퍼 이상이어야 가입할 수 있습니다.")
    private int photoSyncRate;

    @Schema(
        type = "string",
        description = "닮은 동물상",
        example = "강아지상")
    @NotBlank(message = "닮은 동물상은 반드시 입력되어야 합니다.")
    private String lookAlikeAnimal;

    @Schema(
        type = "string",
        description = "선호하는 동물상",
        example = "강아지상")
    @NotBlank(message = "선호하는 동물상은 반드시 입력되어야 합니다.")
    private String preferredAnimal;

    @Schema(
        type = "string",
        description = "선호하는 지역",
        example = "서울")
    @NotBlank(message = "선호하는 지역은 반드시 입력되어야 합니다.")
    private String preferredArea;

    @Schema(
        type = "string",
        description = "선호하는 음역대",
        example = "저음")
    @NotBlank(message = "선호하는 음역대는 반드시 입력되어야 합니다.")
    private String preferredVocalRange;

    @Schema(
        type = "number",
        description = "선호하는 나이 하한",
        example = "22")
    @Positive(message = "선호하는 나이 하한은 양수여야 합니다.")
    private int preferredAgeLowerBound;

    @Schema(
        type = "number",
        description = "선호하는 나이 상한",
        example = "30")
    @Positive(message = "선호하는 나이 상한은 양수여야 합니다.")
    private int preferredAgeUpperBound;

    @Schema(
        type = "number",
        description = "선호하는 키 하한",
        example = "177")
    @Positive(message = "선호하는 키 하한은 양수여야 합니다.")
    private int preferredHeightLowerBound;

    @Schema(
        type = "number",
        description = "선호하는 키 상한",
        example = "185")
    @Positive(message = "선호하는 키 상한은 양수여야 합니다.")
    private int preferredHeightUpperBound;

    @Schema(
        type = "string",
        description = "선호하는 체형",
        example = "마른체형")
    @NotBlank(message = "선호하는 체형은 반드시 입력되어야 합니다.")
    private String preferredBodyType;

    @Schema(
        type = "string",
        description = "선호하는 MBTI",
        example = "ISTJ")
    @Pattern(
        regexp = "^[EI][SN][TF][JP]$",
        message = "유효하지 않은 MBTI 형식입니다.")
    @NotBlank(message = "선호하는 MBTI는 반드시 입력되어야 합니다.")
    private String preferredMbti;
}
