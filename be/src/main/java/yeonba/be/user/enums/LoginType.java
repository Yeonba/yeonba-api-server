package yeonba.be.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {

    KAKAO("카카오"),

    APPLE("애플");

    private final String type;
}
