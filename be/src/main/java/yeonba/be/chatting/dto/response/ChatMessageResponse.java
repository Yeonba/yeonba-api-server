package yeonba.be.chatting.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChatMessageResponse {

    private long userId;
    private String userName;
    private String content;
    private LocalDateTime sentAt;

    public ChatMessageResponse(long userId, String userName, String content, LocalDateTime sentAt) {

        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.sentAt = sentAt;
    }
}
