package yeonba.be.chatting.repository.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;
import yeonba.be.chatting.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
