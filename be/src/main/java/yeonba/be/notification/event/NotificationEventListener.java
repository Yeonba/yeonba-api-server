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

        // 사용자가 알림 수신에 동의했을 때만 푸시 알림 전송
        if (notificationService.isUserAllowedNotification(sendEvent.receiver(), sendEvent.type())) {
            int badge = notificationService.getBadge(sendEvent.receiver().getId());
            fcmUtil.sendNotification(sendEvent, badge);
        }

        notificationService.saveNotification(sendEvent);
    }
}
