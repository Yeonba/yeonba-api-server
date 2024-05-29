package yeonba.be.chatting.dto.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChatPublishRequest implements Serializable {

    private long roomId;
    private long userId;
    private String userName;
    private String content;
    private LocalDateTime sentAt;
}
