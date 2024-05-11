package yeonba.be.notification.repository;

public interface NotificationRepositoryCustom {

    void readNotificationsUpToIdBy(long receiverId, long notificationId);
}
