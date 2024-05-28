package yeonba.be.chatting.repository.chatroom;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.user.entity.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {

    Optional<ChatRoom> findBySenderAndReceiver(User sender, User receiver);

    boolean existsBySenderAndReceiver(User sender, User receiver);
}
