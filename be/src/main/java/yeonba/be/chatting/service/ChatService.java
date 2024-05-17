package yeonba.be.chatting.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeonba.be.chatting.dto.response.ChatRoomResponse;
import yeonba.be.chatting.entity.ChatMessage;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.chatting.repository.chatmessage.ChatMessageQuery;
import yeonba.be.chatting.repository.chatroom.ChatRoomCommand;
import yeonba.be.chatting.repository.chatroom.ChatRoomQuery;
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
    private final ChatMessageQuery chatMessageQuery;
    private final UserQuery userQuery;
    private final BlockQuery blockQuery;

    public List<ChatRoomResponse> getChatRooms(long userId) {

        User user = userQuery.findById(userId);

        List<ChatRoom> chatRooms = chatRoomQuery.findAllBy(user);

        return chatRooms.stream()
            .map(chatRoom -> toChatRoomResponse(chatRoom, user))
            .toList();
    }

    private ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom, User user) {

        User partner = chatRoom.getSentUser().equals(user) ? chatRoom.getReceivedUser()
            : chatRoom.getSentUser();

        ChatMessage lastMessage = chatMessageQuery.findLastMessageByChatRoomId(chatRoom.getId());

        return new ChatRoomResponse(chatRoom.getId(), partner.getNickname(),
            partner.getProfilePhotos().get(0).getPhotoUrl(),
            chatMessageQuery.countUnreadMessagesByChatRoomId(chatRoom.getId()),
            lastMessage.getContent(),
            lastMessage.getSentAt());
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
