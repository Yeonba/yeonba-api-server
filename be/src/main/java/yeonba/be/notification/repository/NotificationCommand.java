package yeonba.be.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.notification.entity.Notification;

@Component
@RequiredArgsConstructor
public class NotificationCommand {

    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification) {

        return notificationRepository.save(notification);
    }

    public void readNotificationsUpToIdBy(long receiverId, long notificationId) {

        notificationRepository.readNotificationsUpToIdBy(receiverId, notificationId);
    }
}
