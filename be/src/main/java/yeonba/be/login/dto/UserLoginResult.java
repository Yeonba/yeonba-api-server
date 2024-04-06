package yeonba.be.login.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginResult {

    private final String accessToken;
    private final String refreshToken;
    private final boolean isInactiveUser;
}
