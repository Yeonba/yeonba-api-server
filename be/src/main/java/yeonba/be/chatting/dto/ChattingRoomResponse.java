package yeonba.be.chatting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChattingRoomResponse {

  @Schema(
      type = "number",
      description = "채팅방 ID",
      example = "1"
  )
  private Long id;

  @Schema(
      type = "string",
      description = "채팅 상대 이름",
      example = "김민재"
  )
  private String partnerName;

  @Schema(
      type = "number",
      description = "읽지 않은 메시지 수",
      example = "25"
  )
  private Integer unreadMessageNumber;

  @Schema(
      type = "string",
      description = "마지막 메시지",
      example = "잘 자!"
  )
  private String lastMessage;

  @Schema(
      type = "string",
      description = "마지막 메시지 일시",
      example = "2022-10-11 13:20:15"
  )
  private LocalDateTime lastMessageAt;
}
