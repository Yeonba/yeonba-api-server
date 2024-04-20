package yeonba.be.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("남", true),
    FEMALE("여", false);

    public final String genderString;
    public final boolean genderBoolean;

    public static Gender from(String genderString) {

        if (StringUtils.equals(genderString, MALE.genderString)) {

            return MALE;
        }

        return FEMALE;
    }
}
