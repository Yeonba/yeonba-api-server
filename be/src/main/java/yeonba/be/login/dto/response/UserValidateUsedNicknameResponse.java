package yeonba.be.login.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserValidateUsedNicknameResponse {

    @Schema(
        type = "boolean",
        description = "닉네임 중복 여부",
        example = "false")
    @JsonProperty("isUsedNickname")
    private boolean usedNickname;
}
