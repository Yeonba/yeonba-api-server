package yeonba.be.chatting.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRequestAcceptResponse {

    @Schema(
        type = "number",
        description = "채팅방 ID",
        example = "1")
    private long chatRoomId;
}
