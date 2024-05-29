package yeonba.be.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import yeonba.be.notification.entity.Notification;

@Getter
@AllArgsConstructor
public class NotificationPageResponse {

    @Schema(
        type = "array",
        description = "알림 목록")
    private List<NotificationResponse> notifications;

    @Schema(
        type = "number",
        description = "전체 페이지 수",
        example = "5")
    private int totalPages;

    @Schema(
        type = "number",
        description = "전체 데이터 수",
        example = "100")
    private long totalElements;

    @Schema(
        type = "boolean",
        description = "첫 페이지 여부",
        example = "false")
    @JsonProperty("isFirst")
    private boolean first;

    @Schema(
        type = "boolean",
        description = "마지막 페이지 여부",
        example = "true")
    @JsonProperty("isLast")
    private boolean last;

    public static NotificationPageResponse from(Page<Notification> page,
        List<NotificationResponse> notifications) {

        return new NotificationPageResponse(
            notifications,
            page.getTotalPages(),
            page.getTotalElements(),
            page.isFirst(),
            page.isLast());
    }
}
