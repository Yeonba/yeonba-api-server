package yeonba.be.mypage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.user.entity.User;

@Table(name = "acquaintances")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Acquaintance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Acquaintance(
        String name,
        String phoneNumber,
        User user) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }
}
