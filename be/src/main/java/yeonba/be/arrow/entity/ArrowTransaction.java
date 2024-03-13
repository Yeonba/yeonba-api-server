package yeonba.be.arrow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yeonba.be.user.entity.User;

@Table(name = "arrows_transactions")
@Getter
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor
public class ArrowTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sent_user_id")
    private User sentUser;

    @ManyToOne
    @JoinColumn(name = "received_user_id")
    private User receivedUser;

    @Column(nullable = false)
    private int arrows;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ArrowTransaction(
        User receivedUser,
        int arrows) {
        this.receivedUser = receivedUser;
        this.arrows = arrows;
    }

    public ArrowTransaction(
        User sentUser,
        User receivedUser,
        int arrows) {

        this.sentUser = sentUser;
        this.receivedUser = receivedUser;
        this.arrows = arrows;
    }
}
