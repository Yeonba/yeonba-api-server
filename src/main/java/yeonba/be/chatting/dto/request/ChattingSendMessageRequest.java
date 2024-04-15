package yeonba.be.chatting.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChattingSendMessageRequest {

  @Schema(
      type = "string",
      description = "메시지 내용",
      example = "밥 먹었어?"
  )
  private String content;
}
