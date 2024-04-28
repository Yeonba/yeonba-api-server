package yeonba.be.notification.event;

import java.time.LocalDateTime;
import yeonba.be.notification.entity.NotificationType;

public record NotificationSendEvent(
    String receiverDeviceToken,
    NotificationType type,
    long creatorId,
    long receiverId,
    String creatorName,
    LocalDateTime createdAt) {

    public String getNotificationTitle() {

        return type.getTitle();
    }

    public String getNotificationMessage() {

        return String.format(type.getMessage(), creatorName);
    }
}
