package yeonba.be.notification.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.notification.entity.NotificationPermission;
import yeonba.be.notification.entity.NotificationType;
import yeonba.be.user.entity.User;

@Repository
public interface NotificationPermissionRepository
    extends JpaRepository<NotificationPermission, Long> {

    Optional<NotificationPermission> findByUserAndType(User user, NotificationType type);
}
