package yeonba.be.user.service;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.user.entity.User;
import yeonba.be.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {

        // 사용자, 프로필 사진, 선호 조건 엔티티 생성 및 저장
        User user = userService.saveUser(request);
        userService.saveProfilePhotos(user, request);
        userService.saveUserPreference(user, request);

        // access token, refresh token 발급
        Date issuedAt = new Date();
        String accessToken = jwtUtil.generateAccessToken(user, issuedAt);
        String refreshToken = jwtUtil.generateRefreshToken(user, issuedAt);

        // 사용자 refresh token 업데이트
        user.updateRefreshToken(refreshToken);

        return new UserJoinResponse(accessToken, refreshToken);
    }
}
