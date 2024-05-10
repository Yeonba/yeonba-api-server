package yeonba.be.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor
public enum LoginType {

    KAKAO("KAKAO"),

    APPLE("APPLE");

    private final String type;

    public static LoginType from(String loginType) {

        if (StringUtils.equals(loginType, KAKAO.type)) {

            return KAKAO;
        }

        return APPLE;
    }
}
