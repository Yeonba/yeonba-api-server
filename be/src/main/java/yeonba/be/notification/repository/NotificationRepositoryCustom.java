package yeonba.be.notification.repository;

public interface NotificationRepositoryCustom {

    void readAllHasIdLessThanEqual(long notificationId);
}
