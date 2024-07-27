package yeonba.be.mypage.fixtures;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import yeonba.be.user.entity.Animal;
import yeonba.be.user.entity.Area;
import yeonba.be.user.entity.ProfilePhoto;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;
import yeonba.be.user.entity.VocalRange;
import yeonba.be.user.enums.Gender;
import yeonba.be.user.enums.LoginType;

public abstract class UserFixtures {

    public static final long USER_ID = 1L;
    public static final long SOCIAL_ID = 12L;
    public static final LoginType LOGIN_TYPE = LoginType.KAKAO;
    public static final Gender GENDER = Gender.MALE;
    public static final String NICKNAME = "사용자닉네임";
    public static final LocalDate BIRTH = LocalDate.now().minusYears(25);
    public static final int HEIGHT = 180;
    public static final String PHONE_NUMBER = "01011112222";
    public static final int ARROW = 30;
    public static final int PHOTO_SYNC_RATE = 85;
    public static final String BODY_TYPE = "통통체형";
    public static final String JOB = "직장인";
    public static final String MBTI = "ISTJ";
    public static final String JWT = "header.payload.signature";

    public static final Animal ANIMAL = new Animal("강아지상");
    public static final VocalRange VOCAL_RANGE = new VocalRange("저음");
    public static final Area AREA = new Area("서울");
    public static final List<String> PROFILE_PHOTO_URLS = List
        .of("profilephoto/1-0", "profilephoto/1-1");

    public static final int AGE_LOWER_BOUND = 20;
    public static final int AGE_UPPER_BOUND = 40;
    public static final int HEIGHT_LOWER_BOUND = 160;
    public static final int HEIGHT_UPPER_BOUND = 180;

    public static User user() {

        int age = Period.between(BIRTH, LocalDate.now()).getYears();
        User user = new User(
            SOCIAL_ID,
            LOGIN_TYPE,
            GENDER.genderBoolean,
            NICKNAME,
            BIRTH,
            age,
            HEIGHT,
            PHONE_NUMBER,
            ARROW,
            PHOTO_SYNC_RATE,
            BODY_TYPE,
            JOB,
            MBTI,
            VOCAL_RANGE,
            ANIMAL,
            AREA
        );

        List<ProfilePhoto> profilePhotos = PROFILE_PHOTO_URLS.stream()
            .map(photoUrl -> new ProfilePhoto(user, photoUrl))
            .toList();
        user.updateProfilePhotos(profilePhotos);

        return user;
    }

    public static UserPreference userPreference() {

        return new UserPreference(
            AGE_LOWER_BOUND,
            AGE_UPPER_BOUND,
            HEIGHT_LOWER_BOUND,
            HEIGHT_UPPER_BOUND,
            MBTI,
            BODY_TYPE,
            user(),
            VOCAL_RANGE,
            AREA,
            ANIMAL
        );
    }
}
