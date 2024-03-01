package yeonba.be.user.entity;

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

@Table(name = "profile_photos")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String photoUrl;
    private LocalDateTime createdAt;

    public ProfilePhoto(User user, String photoUrl, LocalDateTime createdAt) {
        this.user = user;
        this.photoUrl = photoUrl;
        this.createdAt = createdAt;
    }
}
