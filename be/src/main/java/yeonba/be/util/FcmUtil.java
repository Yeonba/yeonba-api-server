package yeonba.be.util;

import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.NotificationException;
import yeonba.be.notification.event.NotificationSendEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmUtil {

    private final FirebaseMessaging firebaseMessaging;

    @Async
    public void sendNotification(NotificationSendEvent sendEvent, int badge) {

        String deviceToken = Optional.ofNullable(sendEvent.receiver().getDeviceToken())
            .orElseThrow(() -> new GeneralException(NotificationException.DEVICE_TOKEN_NOT_FOUND));

        ApnsConfig apnsConfig = ApnsConfig.builder()
            .setAps(
                Aps.builder().setAlert(
                        ApsAlert.builder()
                            .setTitle(sendEvent.getNotificationTitle())
                            .setBody(sendEvent.getNotificationMessage())
                            .build()
                    )
                    .setBadge(badge)
                    .putCustomData("creatorId", sendEvent.creator().getId())
                    .putCustomData("createdAt", sendEvent.createdAt().toString())
                    .build()
            )
            .build();

        Message message = Message.builder()
            .setToken(deviceToken)
            .setApnsConfig(apnsConfig)
            .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
