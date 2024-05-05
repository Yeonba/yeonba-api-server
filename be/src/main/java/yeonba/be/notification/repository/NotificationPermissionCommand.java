package yeonba.be.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.notification.entity.NotificationPermission;

@Component
@RequiredArgsConstructor
public class NotificationPermissionCommand {

    private final NotificationPermissionRepository notificationPermissionRepository;

    public NotificationPermission save(NotificationPermission notificationPermission) {

        return notificationPermissionRepository.save(notificationPermission);
    }
}
