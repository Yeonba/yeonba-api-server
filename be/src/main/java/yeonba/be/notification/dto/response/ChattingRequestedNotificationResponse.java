package yeonba.be.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import yeonba.be.notification.entity.Notification;
import yeonba.be.user.entity.User;

@Getter
public class ChattingRequestedNotificationResponse extends NotificationResponse {

    @Schema(
        type = "boolean",
        description = "채팅 가능 여부",
        example = "false")
    @JsonProperty("canChat")
    private boolean canChat;

    public ChattingRequestedNotificationResponse(
        String notificationType,
        String content,
        long senderId,
        String senderProfilePhotoUrl,
        LocalDateTime createdAt,
        boolean canChat) {

        super(notificationType, content, senderId, senderProfilePhotoUrl, createdAt);
        this.canChat = canChat;
    }

    public static ChattingRequestedNotificationResponse from(Notification notification,
        boolean canChat) {

        User sender = notification.getSender();

        return new ChattingRequestedNotificationResponse(
            notification.getType().name(),
            notification.getContent(),
            sender.getId(),
            sender.getRepresentativeProfilePhoto(),
            notification.getCreatedAt(),
            canChat
        );
    }
}
