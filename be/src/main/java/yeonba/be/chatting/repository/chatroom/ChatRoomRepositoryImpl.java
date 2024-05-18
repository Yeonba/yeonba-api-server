package yeonba.be.chatting.repository.chatroom;


import static yeonba.be.chatting.entity.QChatRoom.chatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.user.entity.User;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoom> findAllByUserAndActiveIsTrue(User user) {

        return queryFactory.selectFrom(chatRoom)
            .where((chatRoom.sender.eq(user)
                .or(chatRoom.receiver.eq(user))
                .and(chatRoom.active.eq(true)))
            )
            .fetch();
    }
}
