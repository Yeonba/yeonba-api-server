package yeonba.be.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.notification.dto.response.NotificationUnreadCountResponse;
import yeonba.be.notification.service.NotificationService;
import yeonba.be.util.CustomResponse;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/unread/count")
    @Operation(summary = "읽지 않은 알림 개수 조회", description = "읽지 않은 알림 개수를 조회할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "읽지 않은 알림 개수 조회 성공")
    public ResponseEntity<CustomResponse<NotificationUnreadCountResponse>> unreadNotificationsCount(
        @RequestAttribute("userId") long userId) {

        NotificationUnreadCountResponse response =
            notificationService.countUnreadNotifications(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }
}
