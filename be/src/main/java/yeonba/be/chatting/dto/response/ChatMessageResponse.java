package yeonba.be.chatting.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private long userId;
    private String userName;
    private String content;
    private LocalDateTime sentAt;
}
