package yeonba.be.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.NotificationException;
import yeonba.be.notification.event.NotificationSendEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmUtil {

    @Value("${NOTIFICATION_ICON_URL}")
    private String notificationIconUrl;

    private final FirebaseMessaging firebaseMessaging;

    @Async
    public void sendNotification(NotificationSendEvent sendEvent) {

        String deviceToken = Optional.ofNullable(sendEvent.receiver().getDeviceToken())
            .orElseThrow(() -> new GeneralException(NotificationException.DEVICE_TOKEN_NOT_FOUND));

        Notification notification = Notification.builder()
            .setImage(notificationIconUrl)
            .setTitle(sendEvent.getNotificationTitle())
            .setBody(sendEvent.getNotificationMessage())
            .build();

        Message message = Message.builder()
            .setToken(deviceToken)
            .setNotification(notification)
            .putData("creatorId", String.valueOf(sendEvent.creator().getId()))
            .putData("createdAt", String.valueOf(sendEvent.createdAt()))
            .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
