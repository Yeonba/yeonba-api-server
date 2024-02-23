package yeonba.be.chatting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.util.CustomResponse;

@Tag(name = "Chatting", description = "채팅 API")
@RestController
public class ChattingController {

  @GetMapping("/chattings")
  public ResponseEntity<CustomResponse<Void>> test() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "채팅 요청",
      description = "다른 사용자에게 채팅을 요청할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "채팅 요청 정상 처리"
  )
  @PostMapping("/users/{userId}/chat")
  public ResponseEntity<CustomResponse<Void>> requestChat(
      @Parameter(description = "사용자 ID", example = "1")
      @PathVariable long userId) {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }
}
