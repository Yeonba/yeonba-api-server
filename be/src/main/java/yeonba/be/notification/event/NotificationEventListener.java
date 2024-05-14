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

        notificationService.saveNotification(sendEvent);

        if (notificationService.canSendNotification(sendEvent.receiver(), sendEvent.type())) {
            int badge = notificationService.getBadge(sendEvent.receiver().getId());
            fcmUtil.sendNotification(sendEvent, badge);
        }
    }
}
