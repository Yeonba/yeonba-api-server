package yeonba.be.chatting.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.chatting.repository.ChatRoomCommand;
import yeonba.be.chatting.repository.ChatRoomQuery;
import yeonba.be.exception.BlockException;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.block.BlockQuery;
import yeonba.be.user.repository.user.UserQuery;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomCommand chatRoomCommand;
    private final ChatRoomQuery chatRoomQuery;
    private final UserQuery userQuery;
    private final BlockQuery blockQuery;

    public void getChatRooms(long userId) {

        User user = userQuery.findById(userId);

        chatRoomQuery.findAllBy(user);
    }

    public void requestChat(long sentUserId, long receivedUserId) {

        User sentUser = userQuery.findById(sentUserId);
        User receivedUser = userQuery.findById(receivedUserId);

        // 차단한 사용자인지 검증
        Optional<Block> block = blockQuery.findByUser(sentUser, receivedUser);

        if (block.isPresent()) {
            throw new GeneralException(BlockException.ALREADY_BLOCKED_USER);
        }

        // 채팅방 생성
        chatRoomCommand.createChatRoom(new ChatRoom(sentUser, receivedUser));
    }
}
