package yeonba.be.notification.event;

import java.time.LocalDateTime;
import yeonba.be.notification.enums.NotificationType;
import yeonba.be.user.entity.User;

public record NotificationSendEvent(
    NotificationType type,
    User sender,
    User receiver,
    LocalDateTime createdAt) {

    public String getNotificationTitle() {

        return type.getTitle();
    }

    public String getNotificationMessage() {

        return type.getFormattedMessage(this.sender.getNickname());
    }
}
