package yeonba.be.chatting.repository.chatmessage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatMessage;
import yeonba.be.chatting.entity.ChatRoom;

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

    public List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom) {

        return chatMessageRepository.findAllByChatRoomOrderBySentAtDesc(chatRoom);
    }
}
