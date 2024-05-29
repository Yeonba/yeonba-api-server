package yeonba.be.chatting.repository.chatmessage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatMessage;
import yeonba.be.chatting.entity.ChatRoom;

@Component
@RequiredArgsConstructor
public class ChatMessageCommand {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage message) {

        return chatMessageRepository.save(message);
    }

    public void deleteAllByChatRoom(ChatRoom chatRoom) {

        chatMessageRepository.deleteAllByChatRoom(chatRoom);
    }
}
