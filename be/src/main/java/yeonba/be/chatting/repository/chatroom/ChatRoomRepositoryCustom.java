package yeonba.be.chatting.repository.chatroom;

import java.util.List;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.user.entity.User;

public interface ChatRoomRepositoryCustom {

    List<ChatRoom> findAllByUserAndActiveIsTrue(User user);

    boolean existsBy(List<User> users);
}
