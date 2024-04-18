package yeonba.be.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class NotificationQuery {

    private final NotificationRepository notificationRepository;

    public long countUnreadNotificationsBy(User receiver) {

        return notificationRepository.countByReceiverAndReadIsFalse(receiver);
    }
}
