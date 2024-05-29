package yeonba.be.chatting.repository.chatroom;

import static yeonba.be.exception.ChatException.NOT_FOUND_CHAT_ROOM;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class ChatRoomQuery {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findById(long id) {

        return chatRoomRepository.findById(id)
            .orElseThrow(() -> new GeneralException(NOT_FOUND_CHAT_ROOM));
    }

    public List<ChatRoom> findAllBy(User user) {

        return chatRoomRepository.findAllByUserAndActiveIsTrue(user);
    }

    public ChatRoom findBy(User sender, User receiver) {

        return chatRoomRepository.findBySenderAndReceiver(sender, receiver)
            .orElseThrow(() -> new GeneralException(NOT_FOUND_CHAT_ROOM));
    }

    public boolean existsBy(User sender, User receiver) {

        return chatRoomRepository.existsBySenderAndReceiver(sender, receiver);
    }
}
