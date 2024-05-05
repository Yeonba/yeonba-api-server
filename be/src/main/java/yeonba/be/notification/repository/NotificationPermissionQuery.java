package yeonba.be.notification.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.notification.entity.NotificationPermission;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class NotificationPermissionQuery {

    private final NotificationPermissionRepository notificationPermissionRepository;

    public List<NotificationPermission> findAllByUser(User user) {

        return notificationPermissionRepository.findAllByUser(user);
    }
}
