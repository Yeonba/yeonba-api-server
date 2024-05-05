package yeonba.be.notification.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.notification.dto.request.NotificationReceivedRequest;
import yeonba.be.notification.dto.response.NotificationPageResponse;
import yeonba.be.notification.dto.response.NotificationUnreadExistResponse;
import yeonba.be.notification.entity.Notification;
import yeonba.be.notification.event.NotificationSendEvent;
import yeonba.be.notification.repository.NotificationCommand;
import yeonba.be.notification.repository.NotificationQuery;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.user.UserQuery;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserQuery userQuery;
    private final NotificationCommand notificationCommand;
    private final NotificationQuery notificationQuery;

    @Transactional
    public NotificationPageResponse getReceivedNotificationsBy(
        long receiverId, NotificationReceivedRequest request) {

        int pageNumber = request.getPage();
        int size = 9;

        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        User receiver = userQuery.findById(receiverId);
        Page<Notification> page =
            notificationQuery.findReceivedNotificationsBy(receiver, pageRequest);

        readNotifications(page.getContent());

        return NotificationPageResponse.from(page);
    }

    private void readNotifications(List<Notification> notifications) {

        notifications.stream()
            .filter(notification -> !notification.isRead())
            .forEach(Notification::read);
    }

    @Transactional
    public void saveNotification(NotificationSendEvent sendEvent) {

        Notification notification = new Notification(
            sendEvent.getNotificationMessage(),
            sendEvent.type(),
            sendEvent.creator(),
            sendEvent.receiver());
        notificationCommand.save(notification);
    }

    @Transactional(readOnly = true)
    public NotificationUnreadExistResponse isUnreadNotificationExist(long receiverId) {

        User receiver = userQuery.findById(receiverId);
        boolean exist = notificationQuery.existsUnreadNotificationsBy(receiver);

        return new NotificationUnreadExistResponse(exist);
    }

    @Transactional(readOnly = true)
    public int getBadge(long userId) {

        User user = userQuery.findById(userId);

        return (int) notificationQuery.countUnreadNotificationsBy(user);
    }
}
