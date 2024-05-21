package yeonba.be.chatting.repository.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatMessage;

@Component
@RequiredArgsConstructor
public class ChatMessageCommand {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage chatMessage) {

        return chatMessageRepository.save(chatMessage);
    }
}
