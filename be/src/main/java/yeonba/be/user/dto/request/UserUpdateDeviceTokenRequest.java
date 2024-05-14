package yeonba.be.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDeviceTokenRequest {

    @Schema(
        type = "string",
        description = "새로운 deviceToken",
        example = "header.payload.signature")
    @NotBlank(message = "device token은 반드시 입력되어야 합니다.")
    private String deviceToken;
}
