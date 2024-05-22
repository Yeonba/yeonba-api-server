package yeonba.be.chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import yeonba.be.chatting.dto.request.ChatSubscribeResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisChattingSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {

            // redis에서 발행된 데이터를 받아 deserialize
            String publishedMessage = (String) redisTemplate.getStringSerializer()
                .deserialize(message.getBody());

            // ChatMessage 객채로 맵핑
            ChatSubscribeResponse response = objectMapper.readValue(publishedMessage, ChatSubscribeResponse.class);

            log.info("Chatting message received: {}", response.getContent());

            // Websocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/chat/sub/room/" + response.getRoomId(), response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
