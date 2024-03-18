package yeonba.be.user.entity;

import jakarta.persistence.Column;
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
  private String email;

  @Column(nullable = false)
  private String encryptedPassword;

  @Column(nullable = false)
  private String salt;

  @Column(nullable = false)
  private String phoneNumber;
  private int arrow;
  private int photoSyncRate;
  private boolean inactiveStatus;

  @Column(nullable = false)
  private String bodyType;

  @Column(nullable = false)
  private String job;

  @Column(nullable = false)
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

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  public User(
      boolean gender,
      String name,
      String nickname,
      LocalDate birth,
      int age,
      int height,
      String email,
      String encryptedPassword,
      String salt,
      String phoneNumber,
      int arrow,
      int photoSyncRate,
      String bodyType,
      String job,
      String mbti,
      VocalRange vocalRange,
      Animal animal,
      Area area,
      List<ProfilePhoto> profilePhotos) {
    this.gender = gender;
    this.name = name;
    this.nickname = nickname;
    this.birth = birth;
    this.age = age;
    this.height = height;
    this.email = email;
    this.encryptedPassword = encryptedPassword;
    this.salt = salt;
    this.phoneNumber = phoneNumber;
    this.arrow = arrow;
    this.photoSyncRate = photoSyncRate;
    this.inactiveStatus = true;
    this.bodyType = bodyType;
    this.job = job;
    this.mbti = mbti;
    this.vocalRange = vocalRange;
    this.animal = animal;
    this.area = area;
    this.profilePhotos = profilePhotos;
  }

  public void validateSameUser(User user) {

    if (!this.equals(user)) {
      throw new IllegalArgumentException("동일한 사용자가 아닙니다.");
    }
  }

  public void validateNotSameUser(User user) {

    if (this.equals(user)) {
      throw new IllegalArgumentException("동일한 사용자입니다.");
    }
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

  public void validateDailyCheck(LocalDate dailyCheckDay) {

    if (this.lastAccessedAt.isAfter(dailyCheckDay.atStartOfDay())) {
      throw new GeneralException(ArrowException.ALREADY_CHECKED_USER);
    }
  }

  public String getRepresentativeProfilePhoto() {

    return this.profilePhotos.get(0).getPhotoUrl();
  }

  public void updateLastAccessedAt(LocalDateTime accessedAt) {

    this.lastAccessedAt = accessedAt;
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

  public String getGender() {
    if (this.gender) {

      return "남";
    }

    return "여";
  }

  public List<String> getProfilePhotoUrls() {

    return this.profilePhotos.stream()
        .map(ProfilePhoto::getPhotoUrl)
        .toList();
  }

  public void changeInactiveStatus(boolean inactiveStatus) {

    this.inactiveStatus = inactiveStatus;
  }
}
