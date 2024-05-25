package yeonba.be.fixture;

import java.time.LocalDate;
import java.util.Optional;
import lombok.Builder;
import org.apache.commons.lang3.RandomStringUtils;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.VocalRange;
import yeonba.be.user.enums.LoginType;

public class UserFixtureFactory {

    private long socialId;
    private LoginType loginType;
    private boolean gender;
    private String nickname;
    private LocalDate birth;
    private int age;
    private int height;
    private String phoneNumber;
    private int arrow;
    private int photoSyncRate;
    private String bodyType;
    private String job;
    private String mbti;
    private VocalRange vocalRange;
    private Animal animal;
    private Area area;

    @Builder
    public UserFixtureFactory(
        Long socialId,
        LoginType loginType,
        Boolean gender,
        String nickname,
        LocalDate birth,
        Integer age,
        Integer height,
        String phoneNumber,
        Integer arrow,
        Integer photoSyncRate,
        String bodyType,
        String job,
        String mbti,
        VocalRange vocalRange,
        Animal animal,
        Area area) {

        this.socialId = defaultValue(socialId, 0L);
        this.nickname = defaultValue(nickname, getRandomNickname());
        this.loginType = defaultValue(loginType, LoginType.KAKAO);
        this.gender = defaultValue(gender, true);
        this.birth = defaultValue(birth, LocalDate.of(2000, 1, 1));
        this.age = defaultValue(age, 25);
        this.height = defaultValue(height, 170);
        this.phoneNumber = defaultValue(phoneNumber, getRandomPhoneNumber());
        this.arrow = defaultValue(arrow, 30);
        this.photoSyncRate = defaultValue(photoSyncRate, 80);
        this.bodyType = defaultValue(bodyType, "마른체형");
        this.job = defaultValue(job, "직장인");
        this.mbti = defaultValue(mbti, "ISTJ");

        this.vocalRange = vocalRange;
        this.animal = animal;
        this.area = area;
    }

    private <T> T defaultValue(T value, T defaultValue) {

        return Optional.ofNullable(value).orElse(defaultValue);
    }

    private String getRandomNickname() {

        return RandomStringUtils.randomAlphabetic(5, 9);
    }

    private String getRandomPhoneNumber() {

        return "010".concat(RandomStringUtils.random(8, "012345678"));
    }

    public User getFixture() {

        return new User(
            this.socialId,
            this.loginType,
            this.gender,
            this.nickname,
            this.birth,
            this.age,
            this.height,
            this.phoneNumber,
            this.arrow,
            this.photoSyncRate,
            this.bodyType,
            this.job,
            this.mbti,
            this.vocalRange,
            this.animal,
            this.area);
    }
}
