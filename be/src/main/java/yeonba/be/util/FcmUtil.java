package yeonba.be.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UtilException;
import yeonba.be.notification.event.NotificationSendEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmUtil {

    private final FirebaseMessaging firebaseMessaging;

    public void sendNotification(NotificationSendEvent sendEvent) {

        Notification notification = Notification.builder()
            .setTitle(sendEvent.getNotificationTitle())
            .setBody(sendEvent.getNotificationMessage())
            .build();

        Message message = Message.builder()
            .setToken(sendEvent.receiverDeviceToken())
            .setNotification(notification)
            .putData("creatorId", String.valueOf(sendEvent.creatorId()))
            .putData("createdAt", String.valueOf(sendEvent.createdAt()))
            .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
            throw new GeneralException(UtilException.FCM_EXCEPTION);
        }
    }
}
