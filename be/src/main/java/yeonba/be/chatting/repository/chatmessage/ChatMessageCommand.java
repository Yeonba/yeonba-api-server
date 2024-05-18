package yeonba.be.chatting.repository.chatmessage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatMessage;

@Component
@RequiredArgsConstructor
public class ChatMessageCommand {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage createChatMessage(ChatMessage message) {

        return chatMessageRepository.save(message);
    }
}
