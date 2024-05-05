package yeonba.be.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.notification.dto.request.NotificationReceivedRequest;
import yeonba.be.notification.dto.response.NotificationPageResponse;
import yeonba.be.notification.dto.response.NotificationUnreadExistResponse;
import yeonba.be.notification.service.NotificationService;
import yeonba.be.util.CustomResponse;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "받은 알림 목록 조회", description = "받은 알림 목록을 조회할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "받은 알림 목록 조회 성공")
    @GetMapping("/users/notifications")
    public ResponseEntity<CustomResponse<NotificationPageResponse>> getReceivedNotifications(
        @RequestAttribute("userId") long userId,
        @Valid @ParameterObject NotificationReceivedRequest request) {

        NotificationPageResponse response =
            notificationService.getReceivedNotificationsBy(userId, request);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "읽지 않은 알림 존재 여부 조회", description = "읽지 않은 알림 존재 여부 확인 가능")
    @ApiResponse(responseCode = "200", description = "읽지 않은 알림 존재 여부 확인 성공")
    @GetMapping("/users/notifications/unread/exists")
    public ResponseEntity<CustomResponse<NotificationUnreadExistResponse>>
    getUnreadNotificationExistence(@RequestAttribute("userId") long userId) {

        NotificationUnreadExistResponse response =
            notificationService.isUnreadNotificationExist(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }
}
