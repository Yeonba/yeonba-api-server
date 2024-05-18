package yeonba.be.chatting.repository.chatmessage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatMessage;

@Component
@RequiredArgsConstructor
public class ChatMessageQuery {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage findLastMessageByChatRoomId(long chatRoomId) {

        return chatMessageRepository.findFirstByChatRoomIdOrderBySentAtDesc(chatRoomId);
    }

    public int countUnreadMessagesByChatRoomId(long chatRoomId) {

        return chatMessageRepository.countByChatRoomIdAndReadIsFalse(chatRoomId);
    }
}
