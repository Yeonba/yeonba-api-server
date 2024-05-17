package yeonba.be.chatting.repository;

import java.util.List;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.user.entity.User;

public interface ChatRoomRepositoryCustom {

    List<ChatRoom> findAllByUserAndActive(User user);
}
