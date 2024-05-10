package yeonba.be.notification.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.notification.entity.NotificationPermission;

@Component
@RequiredArgsConstructor
public class NotificationPermissionCommand {

    private final NotificationPermissionRepository notificationPermissionRepository;

    public void saveAll(List<NotificationPermission> notificationPermissions) {

        notificationPermissionRepository.saveAll(notificationPermissions);
    }
}
