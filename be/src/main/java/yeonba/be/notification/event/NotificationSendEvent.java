package yeonba.be.notification.event;

import java.time.LocalDateTime;
import yeonba.be.notification.entity.NotificationType;
import yeonba.be.user.entity.User;

public record NotificationSendEvent(
    NotificationType type,
    User creator,
    User receiver,
    LocalDateTime createdAt) {

    public String getNotificationTitle() {

        return type.getTitle();
    }

    public String getNotificationMessage() {

        return String.format(type.getMessage(), creator.getName());
    }
}
