package yeonba.be.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import yeonba.be.exception.ArrowException;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.enums.Gender;
import yeonba.be.user.enums.LoginType;

@Table(name = "users")
@Getter
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long socialId;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private boolean gender;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birth;
    private int age;
    private int height;

    @Column(nullable = false)
    private String phoneNumber;
    private int arrow;
    private int photoSyncRate;
    private boolean inactive;

    @Column(nullable = false)
    private String bodyType;

    @Column(nullable = false)
    private String job;

    @Column(nullable = false)
    private String mbti;
    private String refreshToken;

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

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @OneToMany(mappedBy = "blockedUser", fetch = FetchType.LAZY)
    private List<Block> blocks;

    public User(
        long socialId,
        LoginType loginType,
        boolean gender,
        String name,
        String nickname,
        LocalDate birth,
        int age,
        int height,
        String phoneNumber,
        int arrow,
        int photoSyncRate,
        String bodyType,
        String job,
        String mbti,
        VocalRange vocalRange,
        Animal animal,
        Area area) {

        this.socialId = socialId;
        this.loginType = loginType;
        this.gender = gender;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.age = age;
        this.height = height;
        this.phoneNumber = phoneNumber;
        this.arrow = arrow;
        this.photoSyncRate = photoSyncRate;
        this.inactive = false;
        this.bodyType = bodyType;
        this.job = job;
        this.mbti = mbti;
        this.vocalRange = vocalRange;
        this.animal = animal;
        this.area = area;
        this.deleted = false;
    }

    public void validateNotSameUser(User user) {

        if (this.equals(user)) {
            throw new GeneralException(UserException.SAME_USER);
        }
    }

    public void delete() {

        this.deleted = true;
        this.name = "deleted";
        this.nickname = "deleted";
        this.age = 0;
        this.height = 0;
        this.phoneNumber = "deleted";
    }

    public void validateDailyCheck(LocalDate dailyCheckDay) {

        if (this.lastAccessedAt.isAfter(dailyCheckDay.atStartOfDay())) {
            throw new GeneralException(ArrowException.ALREADY_CHECKED_USER);
        }
    }

    public String getRepresentativeProfilePhoto() {

        return this.profilePhotos.get(0).getPhotoUrl();
    }

    public void updateLastAccessedAt(LocalDateTime accessAt) {

        this.lastAccessedAt = accessAt;
    }

    public void plusArrow(int arrow) {

        this.arrow += arrow;
    }

    public void minusArrow(int arrow) {

        if (this.arrow < arrow) {
            throw new GeneralException(ArrowException.NOT_ENOUGH_ARROW_TO_SEND);
        }

        this.arrow -= arrow;
    }

    public String getGenderString() {

        return Gender.genderBooleanToString(this.gender);
    }

    public boolean getGenderBoolean() {

        return this.gender;
    }

    public List<String> getProfilePhotoUrls() {

        return this.profilePhotos.stream()
            .map(ProfilePhoto::getPhotoUrl)
            .toList();
    }

    public void changeInactiveStatus(boolean inactiveStatus) {

        this.inactive = inactiveStatus;
    }

    public void updateProfilePhotos(List<ProfilePhoto> profilePhotos) {

        this.profilePhotos = profilePhotos;
    }

    public void validateRefreshToken(String refreshToken) {

        if (!this.refreshToken.equals(refreshToken)) {
            throw new GeneralException(UserException.INVALID_REFRESH_TOKEN);
        }
    }

    public void updateRefreshToken(String refreshToken) {

        this.refreshToken = refreshToken;
    }

    public void validateSameGender(User user) {

        if (this.gender == user.getGenderBoolean()) {
            throw new GeneralException(UserException.SAME_GENDER_USER);
        }
    }
}
