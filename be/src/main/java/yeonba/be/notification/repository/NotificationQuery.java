package yeonba.be.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import yeonba.be.notification.entity.Notification;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class NotificationQuery {

    private final NotificationRepository notificationRepository;

    public long countUnreadNotificationsBy(User receiver) {

        return notificationRepository.countByReceiverAndReadIsFalse(receiver);
    }

    public boolean existsUnreadNotificationsBy(User receiver) {

        return notificationRepository.existsByReceiverAndReadIsFalse(receiver);
    }

    public Page<Notification> findRecentlyReceivedNotificationsBy(
        User receiver, PageRequest pageRequest) {

        return notificationRepository.findAllByReceiverOrderByCreatedAtDesc(receiver, pageRequest);
    }
}
