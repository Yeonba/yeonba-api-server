package yeonba.be.chatting.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import yeonba.be.chatting.dto.request.ChatPublishRequest;
import yeonba.be.chatting.dto.response.ChatMessageResponse;
import yeonba.be.chatting.dto.response.ChatRequestAcceptResponse;
import yeonba.be.chatting.dto.response.ChatRoomResponse;
import yeonba.be.chatting.service.ChatService;
import yeonba.be.util.CustomResponse;

@Tag(name = "Chatting", description = "채팅 API")
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat")
    public void chat(ChatPublishRequest request) {

        chatService.publish(request);
    }

    @Operation(summary = "채팅 메시지 목록 조회", description = "특정 채팅방의 메시지 목록을 조회할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "채팅 메시지 목록 조회 성공")
    @ResponseBody
    @GetMapping("/chat-rooms/{roomId}/messages")
    public ResponseEntity<CustomResponse<List<ChatMessageResponse>>> getChatMessages(
        @RequestAttribute("userId") long userId,
        @Parameter(description = "채팅방 ID", example = "1")
        @PathVariable long roomId) {

        List<ChatMessageResponse> response = chatService.getChatMessages(userId, roomId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "채팅방 목록 조회", description = "자신이 참여 중인 채팅 목록을 조회할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "참여 중인 채팅 목록 조회 성공")
    @ResponseBody
    @GetMapping("/chat-rooms")
    public ResponseEntity<CustomResponse<List<ChatRoomResponse>>> getChatRooms(
        @RequestAttribute("userId") long userId) {

        List<ChatRoomResponse> response = chatService.getChatRooms(userId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "채팅 요청", description = "다른 사용자에게 채팅을 요청할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "채팅 요청 정상 처리")
    @ResponseBody
    @PostMapping("/users/{partnerId}/chat")
    public ResponseEntity<CustomResponse<Void>> requestChat(
        @RequestAttribute("userId") long userId,
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable long partnerId) {

        chatService.requestChat(userId, partnerId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "채팅 요청 수락", description = "요청받은 채팅을 수락할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "채팅 요청 수락 정상 처리")
    @ResponseBody
    @PostMapping("/notifications/{notificationId}/chat")
    public ResponseEntity<CustomResponse<ChatRequestAcceptResponse>> acceptRequestedChat(
        @RequestAttribute("userId") long userId,
        @Parameter(description = "알림 ID", example = "1")
        @PathVariable long notificationId) {

        ChatRequestAcceptResponse response = chatService.acceptRequestedChat(userId,
            notificationId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(response));
    }

    @Operation(summary = "채팅방 나가기", description = "채팅방을 나갈 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "채팅방 나가기 정상 처리")
    @DeleteMapping("/chat-rooms/{roomId}")
    public ResponseEntity<CustomResponse<Void>> leaveChatRoom(
        @RequestAttribute("userId") long userId,
        @Parameter(description = "채팅방 ID", example = "1")
        @PathVariable long roomId) {

        chatService.leaveChatRoom(userId, roomId);

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }
}
