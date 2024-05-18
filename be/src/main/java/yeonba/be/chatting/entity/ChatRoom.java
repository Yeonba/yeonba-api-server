package yeonba.be.chatting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yeonba.be.user.entity.User;

@Table(name = "chat_rooms")
@Getter
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_user_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_user_id")
    private User receiver;

    private boolean active;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages;

    public ChatRoom(User sender, User receiver) {

        this.sender = sender;
        this.receiver = receiver;
        this.active = false;
    }

    public void activeRoom() {

        this.active = true;
    }
}
