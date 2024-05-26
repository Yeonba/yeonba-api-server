package yeonba.be.notification.dto.response;

import java.time.LocalDateTime;
import yeonba.be.notification.entity.Notification;
import yeonba.be.user.entity.User;

public class SimpleNotificationResponse extends NotificationResponse {

    public SimpleNotificationResponse(
        String notificationType,
        String content,
        long senderId,
        String senderProfilePhotoUrl,
        String senderNickname,
        LocalDateTime createdAt) {

        super(notificationType, content, senderId, senderProfilePhotoUrl, senderNickname,
            createdAt);
    }

    public static SimpleNotificationResponse from(Notification notification) {

        User sender = notification.getSender();

        return new SimpleNotificationResponse(
            notification.getType().name(),
            notification.getContent(),
            sender.getId(),
            sender.getRepresentativeProfilePhoto(),
            sender.getNickname(),
            notification.getCreatedAt()
        );
    }
}
