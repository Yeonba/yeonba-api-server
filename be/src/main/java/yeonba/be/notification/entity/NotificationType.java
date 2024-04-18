package yeonba.be.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    ARROW_RECEIVED("화살 수신"),
    CHAT_REQUESTED("채팅 요청 수신"),
    CHAT_REQUEST_ACCEPTED("요청한 채팅 수락됨");

    private final String type;
}
