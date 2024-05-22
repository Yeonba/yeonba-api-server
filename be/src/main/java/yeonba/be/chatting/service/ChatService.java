package yeonba.be.chatting.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.chatting.dto.request.ChatPublishRequest;
import yeonba.be.chatting.dto.response.ChatMessageResponse;
import yeonba.be.chatting.dto.response.ChatRoomResponse;
import yeonba.be.chatting.entity.ChatMessage;
import yeonba.be.chatting.entity.ChatRoom;
import yeonba.be.chatting.repository.chatmessage.ChatMessageCommand;
import yeonba.be.chatting.repository.chatmessage.ChatMessageQuery;
import yeonba.be.chatting.repository.chatroom.ChatRoomCommand;
import yeonba.be.chatting.repository.chatroom.ChatRoomQuery;
import yeonba.be.chatting.repository.chatroom.ChatRoomRepository;
import yeonba.be.exception.BlockException;
import yeonba.be.exception.ChatException;
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
    private final RedisChattingPublisher redisChattingPublisher;
    private final RedisChattingSubscriber adapter;
    private final RedisMessageListenerContainer container;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void publish(ChatPublishRequest request) {

        ChatRoom chatRoom = chatRoomQuery.findById(request.getRoomId());
        User sender = userQuery.findById(request.getUserId());
        User receiver = chatRoom.getSender().equals(sender) ? chatRoom.getReceiver()
            : chatRoom.getSender();

        // TODO: 메시지 Pub/Sub과 메시지 저장 로직 비동기 처리(id, user 등 request, response 변경 가능)
        redisChattingPublisher.publish(new ChannelTopic(String.valueOf(request.getRoomId())),
            request);
        chatMessageCommand.save(
            new ChatMessage(chatRoom, sender, receiver, request.getContent(), request.getSentAt()));
    }

    public List<ChatMessageResponse> getChatMessages(long userId, long roomId) {

        User user = userQuery.findById(userId);

        ChatRoom chatRoom = chatRoomQuery.findById(roomId);

        if (!user.equals(chatRoom.getSender()) && !user.equals(chatRoom.getReceiver())) {
            throw new GeneralException(ChatException.NOT_YOUR_CHAT_ROOM);
        }

        List<ChatMessage> chatMessages = chatMessageQuery.findAllByChatRoom(chatRoom);

        return chatMessages.stream()
            .map(chatMessage -> new ChatMessageResponse(chatMessage.getSender().getId(),
                chatMessage.getSender().getNickname(),
                chatMessage.getContent(), chatMessage.getSentAt()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms(long userId) {

        User user = userQuery.findById(userId);

        List<ChatRoom> chatRooms = chatRoomQuery.findAllBy(user);

        return chatRooms.stream()
            .map(chatRoom -> toChatRoomResponseBy(chatRoom, user))
            .toList();
    }

    private ChatRoomResponse toChatRoomResponseBy(ChatRoom chatRoom, User user) {

        User partner = chatRoom.getSender().equals(user) ? chatRoom.getReceiver()
            : chatRoom.getSender();

        ChatMessage lastMessage = chatMessageQuery.findLastMessageByChatRoomId(chatRoom.getId());

        return new ChatRoomResponse(chatRoom.getId(), partner.getNickname(),
            partner.getRepresentativeProfilePhoto(),
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

        // 비활성화된 채팅방 생성
        chatRoomCommand.createChatRoom(new ChatRoom(sender, receiver));

        NotificationSendEvent notificationSendEvent = new NotificationSendEvent(
            NotificationType.CHATTING_REQUESTED, sender, receiver,
            LocalDateTime.now());

        eventPublisher.publishEvent(notificationSendEvent);
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

            throw new GeneralException(
                NotificationException.NOT_YOUR_CHATTING_REQUEST_NOTIFICATION);
        }

        // 채팅방 활성화
        ChatRoom chatRoom = chatRoomQuery.findBy(sender, receiver);
        chatRoom.activeRoom();

        String activeRoom = "채팅방이 생성되었습니다.";

        chatMessageCommand.save(
            new ChatMessage(chatRoom, sender, receiver, activeRoom, LocalDateTime.now()));

        // 메시지 수신을 위한 Redis Pub/Sub 구독
        container.addMessageListener(adapter, new ChannelTopic(String.valueOf(chatRoom.getId())));

        NotificationSendEvent notificationSendEvent = new NotificationSendEvent(
            NotificationType.CHATTING_REQUEST_ACCEPTED, receiver, sender,
            LocalDateTime.now());

        eventPublisher.publishEvent(notificationSendEvent);
    }
}
