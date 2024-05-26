package yeonba.be.chatting.repository.chatroom;


import static yeonba.be.chatting.entity.QChatRoom.chatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean existsBy(List<User> users) {

        User sender = users.get(0);
        User receiver = users.get(1);

        Integer result = queryFactory.selectOne().from(chatRoom)
            .where(
                chatRoom.sender.eq(sender).and(chatRoom.receiver.eq(receiver))
                    .or(chatRoom.receiver.eq(sender).and(chatRoom.sender.eq(receiver)))
            )
            .fetchFirst();

        return Objects.nonNull(result);
    }
}
