package yeonba.be.login.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "verification_codes")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public VerificationCode(
        String phoneNumber,
        String code,
        LocalDateTime expiredAt) {

        this.phoneNumber = phoneNumber;
        this.code = code;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired(LocalDateTime now) {

        return this.expiredAt.isBefore(now);
    }
}
