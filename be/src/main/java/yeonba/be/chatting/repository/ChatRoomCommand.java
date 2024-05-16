package yeonba.be.chatting.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatRoom;

@Component
@RequiredArgsConstructor
public class ChatRoomCommand {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(ChatRoom chatRoom) {

        return chatRoomRepository.save(chatRoom);
    }
}
