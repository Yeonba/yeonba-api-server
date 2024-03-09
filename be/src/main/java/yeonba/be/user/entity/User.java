package yeonba.be.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeonba.be.exception.ExceptionType;
import yeonba.be.exception.GeneralException;

@Table(name = "users")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean gender;
    private String name;
    private String nickname;
    private LocalDate birth;
    private int height;
    private String email;
    private String encryptedPassword;
    private String phoneNumber;
    private int arrow;
    private double photoSyncRate;
    private boolean inactiveStatus;
    private String bodyType;
    private String job;
    private String drinkingHabit;
    private String smokingHabit;
    private String mbti;

    @ManyToOne
    @JoinColumn(name = "vocal_range_id")
    private VocalRange vocalRange;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<ProfilePhoto> profilePhotos;

    private LocalDateTime lastAccessedAt;
    private LocalDateTime deletedAt;

    public User(
        String name,
        String nickname,
        LocalDate birth,
        int height,
        String email,
        String encryptedPassword,
        String phoneNumber,
        int arrow,
        double photoSyncRate,
        String bodyType,
        String job,
        String drinkingHabit,
        String smokingHabit,
        String mbti,
        LocalDateTime lastAccessedAt) {

        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.height = height;
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.phoneNumber = phoneNumber;
        this.arrow = arrow;
        this.photoSyncRate = photoSyncRate;
        this.inactiveStatus = true;
        this.bodyType = bodyType;
        this.job = job;
        this.drinkingHabit = drinkingHabit;
        this.smokingHabit = smokingHabit;
        this.mbti = mbti;
        this.lastAccessedAt = lastAccessedAt;
    }

    public void changePassword(String encryptedNewPassword) {

        this.encryptedPassword = encryptedNewPassword;
    }

    /**
     * 삭제된 사용자인지 검증
     */
    public void validateDeletedUser(LocalDateTime now) {

        if (this.deletedAt.isAfter(now)) {
            throw new IllegalArgumentException("삭제된 사용자입니다.");
        }
    }

    public String getRepresentativeProfilePhoto() {

        return this.profilePhotos.get(0).getPhotoUrl();
    }

    public void updateLastAccessedAt(LocalDateTime accessedAt) {

        this.lastAccessedAt = accessedAt;
    }

    public void addArrow(int arrow) {

        this.arrow += arrow;
    }

    public void minusArrow(int arrow) {
        if (this.arrow < arrow) {
            throw new GeneralException(ExceptionType.NOT_ENOUGH_ARROW_TO_SEND);
        }

        this.arrow -= arrow;
    }
}
