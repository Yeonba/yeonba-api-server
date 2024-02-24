package yeonba.be.chatting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yeonba.be.chatting.dto.ChattingRoomResponse;
import yeonba.be.chatting.dto.request.ChattingSendMessageRequest;
import yeonba.be.util.CustomResponse;

@Tag(name = "Chatting", description = "채팅 API")
@RestController
public class ChattingController {

  @Operation(
      summary = "채팅 목록 조회",
      description = "자신이 참여 중인 채팅 목록을 조회할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "참여 중인 채팅 목록 조회 성공"
  )
  @GetMapping("/chattings")
  public ResponseEntity<CustomResponse<List<ChattingRoomResponse>>> chattings() {

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

  @Operation(
      summary = "채팅 메시지 전송",
      description = "단순 텍스트 형식 채팅 메시지를 전송할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "202",
      description = "채팅 메시지 전송 정상 처리"
  )
  @PostMapping("/chattings/{chattingId}/message")
  public ResponseEntity<CustomResponse<Void>> sendMessage(
      @Parameter(description = "채팅방 ID", example = "1")
      @PathVariable long chattingId,
      @RequestBody ChattingSendMessageRequest request) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "사진 메시지 전송",
      description = "사진을 전송할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "202",
      description = "사진 전송 정상 처리"
  )
  @PostMapping(
      path = "/chattings/{chattingId}/photo",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<CustomResponse<Void>> sendPhoto(
      @Parameter(description = "채팅방 ID", example = "1")
      @PathVariable long chattingId,
      @Parameter(description = "전송 사진 파일")
      @RequestPart(value = "photoFile") MultipartFile photoFile) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }
}
