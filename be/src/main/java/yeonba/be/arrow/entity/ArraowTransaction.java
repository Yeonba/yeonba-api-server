package yeonba.be.arrow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.user.entity.User;

@Table(name = "arrows_transactions")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ArraowTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sent_user_id")
    private User sentUser;

    @ManyToOne
    @JoinColumn(name = "received_user_id")
    private User receivedUser;

    private LocalDateTime createdAt;

    public ArraowTransaction(User sentUser, User receivedUser) {

        this.sentUser = sentUser;
        this.receivedUser = receivedUser;
    }
}
