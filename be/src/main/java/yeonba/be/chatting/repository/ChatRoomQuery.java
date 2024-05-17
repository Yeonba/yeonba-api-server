package yeonba.be.chatting.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class ChatRoomQuery {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAllBy(User user) {

        return chatRoomRepository.findAllByUserAndActive(user);
    }
}
