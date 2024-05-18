package yeonba.be.chatting.repository.chatmessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.chatting.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findFirstByChatRoomIdOrderBySentAtDesc(long chatRoomId);

    int countByChatRoomIdAndReadIsFalse(long chatRoomId);
}
