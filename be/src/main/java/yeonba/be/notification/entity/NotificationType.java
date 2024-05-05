package yeonba.be.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    ARROW_RECEIVED("화살 수신", "%s님이 화살을 보냈어요!"),
    CHATTING_REQUESTED("채팅 요청 수신", "%s님이 채팅을 요청했어요!"),
    CHATTING_REQUEST_ACCEPTED("요청한 채팅 수락됨", "%s님이 채팅을 수락했어요!");

    private final String title;
    private final String message;

    public String getFormattedMessage(String username) {

        return String.format(this.message, username);
    }
}
