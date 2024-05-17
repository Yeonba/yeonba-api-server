package yeonba.be.chatting.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.chatting.dto.response.ChatRoomResponse;
import yeonba.be.chatting.entity.ChatMessage;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.chatting.repository.chatmessage.ChatMessageCommand;
import yeonba.be.chatting.repository.chatmessage.ChatMessageQuery;
import yeonba.be.chatting.repository.chatroom.ChatRoomCommand;
import yeonba.be.chatting.repository.chatroom.ChatRoomQuery;
import yeonba.be.exception.BlockException;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.NotificationException;
import yeonba.be.notification.entity.Notification;
import yeonba.be.notification.enums.NotificationType;
import yeonba.be.notification.event.NotificationSendEvent;
import yeonba.be.notification.repository.NotificationQuery;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.block.BlockQuery;
import yeonba.be.user.repository.user.UserQuery;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomCommand chatRoomCommand;
    private final ChatRoomQuery chatRoomQuery;
    private final ChatMessageCommand chatMessageCommand;
    private final ChatMessageQuery chatMessageQuery;
    private final UserQuery userQuery;
    private final BlockQuery blockQuery;
    private final NotificationQuery notificationQuey;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms(long userId) {

        User user = userQuery.findById(userId);

        List<ChatRoom> chatRooms = chatRoomQuery.findAllBy(user);

        return chatRooms.stream()
            .map(chatRoom -> toChatRoomResponseBy(chatRoom, user))
            .toList();
    }

    private ChatRoomResponse toChatRoomResponseBy(ChatRoom chatRoom, User user) {

        User partner = chatRoom.getSentUser().equals(user) ? chatRoom.getReceivedUser()
            : chatRoom.getSentUser();

        ChatMessage lastMessage = chatMessageQuery.findLastMessageByChatRoomId(chatRoom.getId());

        return new ChatRoomResponse(chatRoom.getId(), partner.getNickname(),
            partner.getProfilePhotos().get(0).getPhotoUrl(),
            chatMessageQuery.countUnreadMessagesByChatRoomId(chatRoom.getId()),
            lastMessage.getContent(),
            lastMessage.getSentAt());
    }

    @Transactional
    public void requestChat(long senderId, long receiverId) {

        User sender = userQuery.findById(senderId);
        User receiver = userQuery.findById(receiverId);

        // 차단한 사용자인지 검증
        Optional<Block> block = blockQuery.findByUser(sender, receiver);

        if (block.isPresent()) {
            throw new GeneralException(BlockException.ALREADY_BLOCKED_USER);
        }

        NotificationSendEvent notificationSendEvent = new NotificationSendEvent(
            NotificationType.CHATTING_REQUESTED, sender, receiver,
            LocalDateTime.now());

        eventPublisher.publishEvent(notificationSendEvent);

        // 비활성화된 채팅방 생성
        chatRoomCommand.createChatRoom(new ChatRoom(sender, receiver));
    }

    public void acceptRequestedChat(long userId, long notificationId) {

        Notification notification = notificationQuey.findById(notificationId);

        // 채팅 요청 알림인지 검증
        if (!notification.getType().isChattingRequest()) {

            throw new GeneralException(NotificationException.IS_NOT_CHATTING_REQUEST_NOTIFICATION);
        }

        User sender = userQuery.findById(notification.getSender().getId());
        User receiver = userQuery.findById(notification.getReceiver().getId());

        // 본인에게 온 채팅 요청인지 검증
        if (receiver.equals(userQuery.findById(userId))) {

            throw new GeneralException(NotificationException.NOT_YOUR_CHATTING_REQUEST_NOTIFICATION);
        }

        NotificationSendEvent notificationSendEvent = new NotificationSendEvent(
            NotificationType.CHATTING_REQUEST_ACCEPTED, receiver, sender,
            LocalDateTime.now());

        eventPublisher.publishEvent(notificationSendEvent);

        // 채팅방 활성화
        ChatRoom chatRoom = chatRoomQuery.findBy(sender, receiver);
        chatRoom.activeRoom();

        String activeRoom = "채팅방이 활상화되었습니다.";
        chatMessageCommand.createChatMessage(new ChatMessage(chatRoom, sender, receiver, activeRoom));
    }
}
