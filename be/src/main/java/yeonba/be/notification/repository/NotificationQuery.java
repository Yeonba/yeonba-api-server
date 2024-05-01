package yeonba.be.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import yeonba.be.notification.dto.response.NotificationResponse;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class NotificationQuery {

    private final NotificationRepository notificationRepository;

    public long countUnreadNotificationsBy(User receiver) {

        return notificationRepository.countByReceiverAndReadIsFalse(receiver);
    }

    public Page<NotificationResponse> findReceivedNotificationsBy(
        long receiverId,
        PageRequest pageRequest) {

        return notificationRepository.findBy(receiverId, pageRequest);
    }

    public boolean existsUnreadNotificationsBy(User receiver) {

        return notificationRepository.existsByReceiverAndReadIsFalse(receiver);
    }
}
