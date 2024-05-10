package yeonba.be.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import yeonba.be.notification.entity.Notification;
import yeonba.be.user.entity.User;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    @Schema(
        type = "number",
        description = "알림 ID",
        example = "1")
    private long id;

    @Schema(
        type = "string",
        description = """
            알림 타입, 다음 종류 존재
            - ARROW_RECEIVED (화살 수신)
            - CHAT_REQUESTED (채팅 요청 수신)
            - CHAT_REQUEST_ACCEPTED (요청한 채팅 수락됨)""",
        example = "ARROW_RECEIVED")
    private String notificationType;

    @Schema(
        type = "string",
        description = "알림 내용",
        example = "민재님이 화살을 보냈어요!")
    private String content;

    @Schema(
        type = "number",
        description = "알림 보낸 사용자 ID",
        example = "1")
    private long senderId;

    @Schema(
        type = "number",
        description = "알림 보낸 사용자 대표 프로필 사진 URL",
        example = "profile-photo/1-0")
    private String senderProfilePhotoUrl;

    @Schema(
        type = "string",
        description = "알림 보낸 사용자 별명",
        example = "안민재")
    private String senderNickname;

    @Schema(
        type = "string",
        description = "알림 생성 일시",
        example = "2024-04-10 10:12:00.112233")
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {

        User sender = notification.getSender();

        return new NotificationResponse(
            notification.getId(),
            notification.getType().name(),
            notification.getContent(),
            sender.getId(),
            sender.getRepresentativeProfilePhoto(),
            sender.getNickname(),
            notification.getCreatedAt()
        );
    }
}
