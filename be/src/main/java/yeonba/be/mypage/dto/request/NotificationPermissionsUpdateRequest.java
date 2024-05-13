package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.mypage.dto.NotificationPermissionDetail;

@Getter
@NoArgsConstructor
public class NotificationPermissionsUpdateRequest {

    @Schema(
        type = "array",
        description = "알림 유형별 동의 여부")
    @NotEmpty(message = "알림 유형별 동의 여부는 반드시 포함되어야 합니다.")
    List<NotificationPermissionDetail> permissions;
}
