package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserValidateUsedNicknameRequest {

    @Parameter(
        name = "nickname",
        description = "닉네임",
        example = "존존예녀",
        in = ParameterIn.QUERY)
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣]{1,8}$",
        message = "닉네임은 공백 없이 영어 대소문자,한글,숫자로 구성되어야 하며 최대 8자까지 가능합니다.")
    @NotBlank(message = "닉네임은 반드시 입력되어야 합니다.")
    private String nickname;
}
