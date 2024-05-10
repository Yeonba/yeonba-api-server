package yeonba.be.notification.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.NotificationException;
import yeonba.be.notification.dto.request.NotificationPageRequest;
import yeonba.be.notification.dto.response.NotificationPageResponse;
import yeonba.be.notification.dto.response.NotificationUnreadExistResponse;
import yeonba.be.notification.entity.Notification;
import yeonba.be.notification.entity.NotificationPermission;
import yeonba.be.notification.enums.NotificationType;
import yeonba.be.notification.event.NotificationSendEvent;
import yeonba.be.notification.repository.NotificationCommand;
import yeonba.be.notification.repository.NotificationPermissionCommand;
import yeonba.be.notification.repository.NotificationPermissionQuery;
import yeonba.be.notification.repository.NotificationQuery;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.user.UserQuery;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserQuery userQuery;
    private final NotificationQuery notificationQuery;
    private final NotificationPermissionQuery notificationPermissionQuery;

    private final NotificationCommand notificationCommand;
    private final NotificationPermissionCommand notificationPermissionCommand;

    @Transactional
    public NotificationPageResponse getRecentlyReceivedNotificationsBy(
        long receiverId, NotificationPageRequest request) {

        // 페이지 번호는 optional, default 첫 페이지 제공
        int pageNumber = Optional.ofNullable(request.getPage()).orElse(0);
        int size = 45;

        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        User receiver = userQuery.findById(receiverId);
        Page<Notification> page =
            notificationQuery.findRecentlyReceivedNotificationsBy(receiver, pageRequest);

        // 가장 최근에 받은 알림 ID 도출
        long mostRecentNotificationId = page.getContent().stream()
            .mapToLong(Notification::getId)
            .max()
            .orElse(Long.MAX_VALUE);

        // 가장 최근에 받은 알림 포함 이전 알림 전부 읽음 처리
        notificationCommand.readAllHasIdLessThanEqual(mostRecentNotificationId);

        return NotificationPageResponse.from(page);
    }

    @Transactional
    public void saveNotification(NotificationSendEvent sendEvent) {

        Notification notification = new Notification(
            sendEvent.getNotificationMessage(),
            sendEvent.type(),
            sendEvent.sender(),
            sendEvent.receiver());
        notificationCommand.save(notification);
    }

    public void saveAllowedNotificationPermissions(User user) {

        List<NotificationPermission> notificationPermissions =
            Arrays.stream(NotificationType.values())
                .map(type -> new NotificationPermission(type, user))
                .toList();
        notificationPermissionCommand.saveAll(notificationPermissions);
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

    @Transactional(readOnly = true)
    public boolean canSendNotification(User receiver, NotificationType type) {

        Optional<String> deviceToken = Optional.ofNullable(receiver.getDeviceToken());
        if (deviceToken.isEmpty()) {
            throw new GeneralException(NotificationException.DEVICE_TOKEN_NOT_FOUND);
        }

        NotificationPermission notificationPermission =
            notificationPermissionQuery.findByUserAndType(receiver, type);

        return notificationPermission.getPermissionStatus();
    }
}
