package yeonba.be.chatting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import yeonba.be.chatting.entity.ChatRoom;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {

    @Schema(type = "number", description = "채팅방 ID", example = "1")
    private long id;

    @Schema(type = "string", description = "채팅 상대 이름", example = "김민재")
    private String partnerName;

    @Schema(type = "string", description = "채팅 상대 프로필 이미지 URL", example = "yeonba.com/profile")
    private String partnerProfileImageUrl;

    @Schema(type = "number", description = "읽지 않은 메시지 수", example = "25")
    private int unreadMessageNumber;

    @Schema(type = "string", description = "마지막 메시지", example = "잘 자!")
    private String lastMessage;

    @Schema(type = "string", description = "마지막 메시지 일시", example = "2022-10-11 13:20:15")
    private LocalDateTime lastMessageAt;
}
