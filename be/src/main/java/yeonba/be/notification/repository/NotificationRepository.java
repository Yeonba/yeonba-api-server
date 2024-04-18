package yeonba.be.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.notification.entity.Notification;
import yeonba.be.user.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByReceiverAndReadIsFalse(User user);

}
