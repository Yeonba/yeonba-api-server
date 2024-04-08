package yeonba.be.user.enums;

import org.apache.commons.lang3.StringUtils;

public enum Gender {

    MALE("남", true),
    FEMALE("여", false);

    public final String genderString;
    public final boolean genderBoolean;

    Gender(String genderString, boolean genderBoolean) {
        this.genderString = genderString;
        this.genderBoolean = genderBoolean;
    }

    public static Gender from(String genderString) {

        if (StringUtils.equals(genderString, MALE.genderString)) {

            return MALE;
        }

        return FEMALE;
    }
}
