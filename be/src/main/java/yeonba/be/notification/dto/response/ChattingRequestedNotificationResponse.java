package yeonba.be.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import yeonba.be.notification.entity.Notification;
import yeonba.be.user.entity.User;

@Getter
public class ChattingRequestedNotificationResponse extends NotificationResponse {

    @Schema(
        type = "number",
        description = "알림 ID",
        example = "1")
    private long notificationId;

    public ChattingRequestedNotificationResponse(
        String notificationType,
        String content,
        long senderId,
        String senderProfilePhotoUrl,
        LocalDateTime createdAt,
        long notificationId) {

        super(notificationType, content, senderId, senderProfilePhotoUrl, createdAt);
        this.notificationId = notificationId;
    }

    public static ChattingRequestedNotificationResponse from(Notification notification) {

        User sender = notification.getSender();

        return new ChattingRequestedNotificationResponse(
            notification.getType().name(),
            notification.getContent(),
            sender.getId(),
            sender.getRepresentativeProfilePhoto(),
            notification.getCreatedAt(),
            notification.getId()
        );
    }
}
