package yeonba.be.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import yeonba.be.notification.entity.Notification;
import yeonba.be.user.entity.User;

@Getter
public class ChattingAcceptedNotificationResponse extends NotificationResponse {

    @Schema(
        type = "number",
        description = "채팅방 ID",
        example = "1")
    private long chatRoomId;

    public ChattingAcceptedNotificationResponse(
        String notificationType,
        String content,
        long senderId,
        String senderProfilePhotoUrl,
        LocalDateTime createdAt,
        long chatRoomId) {

        super(notificationType, content, senderId, senderProfilePhotoUrl, createdAt);
        this.chatRoomId = chatRoomId;
    }

    public static ChattingAcceptedNotificationResponse from(Notification notification,
        long chatRoomId) {

        User sender = notification.getSender();

        return new ChattingAcceptedNotificationResponse(
            notification.getType().name(),
            notification.getContent(),
            sender.getId(),
            sender.getRepresentativeProfilePhoto(),
            notification.getCreatedAt(),
            chatRoomId
        );
    }
}
