package yeonba.be.login.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserValidateUsedPhoneNumberResponse {

    @Schema(
        type = "boolean",
        description = "전화번호 사용 여부",
        example = "false")
    @JsonProperty("isUsedPhoneNumber")
    private boolean usedPhoneNumber;
}
