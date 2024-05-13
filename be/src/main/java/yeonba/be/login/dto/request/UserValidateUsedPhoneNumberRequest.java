package yeonba.be.login.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserValidateUsedPhoneNumberRequest {

    @Parameter(
        name = "phoneNumber",
        description = "전화번호",
        example = "01011112222",
        in = ParameterIn.QUERY)
    @Pattern(
        regexp = "^010\\d{8}$",
        message = "전화번호는 11자리 010으로 시작하며 하이픈(-) 없이 0~9의 숫자로 이뤄져야 합니다.")
    @NotBlank(message = "전화번호는 반드시 입력되어야 합니다.")
    private String phoneNumber;
}
