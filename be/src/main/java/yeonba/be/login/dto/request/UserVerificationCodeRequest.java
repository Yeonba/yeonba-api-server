package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserVerificationCodeRequest {

    @Schema(
        type = "string",
        description = "전화번호",
        example = "01011112222")
    @Pattern(
        regexp = "^010\\d{8}$",
        message = "전화번호는 11자리 010으로 시작하며 하이픈(-) 없이 0~9의 숫자로 이뤄져야 합니다.")
    @NotBlank(message = "전화번호는 반드시 입력되어야 합니다.")
    private String phoneNumber;
}
