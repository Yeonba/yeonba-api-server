package yeonba.be.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.NotificationException;
import yeonba.be.notification.entity.NotificationPermission;
import yeonba.be.notification.entity.NotificationType;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class NotificationPermissionQuery {

    private final NotificationPermissionRepository notificationPermissionRepository;

    public NotificationPermission findBy(User user, NotificationType type) {

        return notificationPermissionRepository.findByUserAndType(user, type)
            .orElseThrow(() ->
                new GeneralException(NotificationException.NOTIFICATION_PERMISSION_NOT_FOUND));
    }
}
