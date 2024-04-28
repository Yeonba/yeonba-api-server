package yeonba.be.notification.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import yeonba.be.notification.service.NotificationService;
import yeonba.be.util.FcmUtil;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final FcmUtil fcmUtil;
    private final NotificationService notificationService;

    @EventListener
    public void sendNotification(NotificationSendEvent sendEvent) {

        fcmUtil.sendNotification(sendEvent);
        notificationService.saveNotification(sendEvent);
    }
}
