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

    public List<ChatRoom> findAllBy(User user) {

        return chatRoomRepository.findAllByUserAndActiveIsTrue(user);
    }

    public ChatRoom findBy(User sentUser, User receivedUser) {

        return chatRoomRepository.findBySentUserAndReceivedUser(sentUser, receivedUser)
            .orElseThrow(() -> new GeneralException(NOT_FOUND_CHAT_ROOM));
    }
}
