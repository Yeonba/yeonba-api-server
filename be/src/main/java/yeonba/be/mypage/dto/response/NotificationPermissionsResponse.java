package yeonba.be.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import yeonba.be.mypage.dto.NotificationPermissionDetail;

@Getter
@AllArgsConstructor
public class NotificationPermissionsResponse {

    @Schema(
        type = "array",
        description = "알림 유형별 동의 내역")
    List<NotificationPermissionDetail> permissions;
}
