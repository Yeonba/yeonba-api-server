package yeonba.be.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.notification.dto.response.NotificationUnreadCountResponse;
import yeonba.be.notification.repository.NotificationQuery;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserQuery userQuery;
    private final NotificationQuery notificationQuery;

    @Transactional(readOnly = true)
    public NotificationUnreadCountResponse countUnreadNotifications(long userId) {

        User receiver = userQuery.findById(userId);
        long unreadNotificationsCount = notificationQuery.countUnreadNotificationsBy(receiver);

        return new NotificationUnreadCountResponse(unreadNotificationsCount);
    }
}
