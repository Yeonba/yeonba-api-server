package yeonba.be.login.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    @Schema(description = "JWT", example = "header.payload.signature")
    private String jwt;
}
