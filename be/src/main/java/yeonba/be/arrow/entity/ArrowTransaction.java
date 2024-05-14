package yeonba.be.arrow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import yeonba.be.arrow.enums.ArrowTransactionType;
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
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(nullable = false)
    private int arrows;

    @Enumerated(EnumType.STRING)
    private ArrowTransactionType type;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ArrowTransaction(
        ArrowTransactionType type,
        User receiver,
        int arrows) {

        this.type = type;
        this.receiver = receiver;
        this.arrows = arrows;
    }

    public ArrowTransaction(
        ArrowTransactionType type,
        User sender,
        User receiver,
        int arrows) {

        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.arrows = arrows;
    }
}
