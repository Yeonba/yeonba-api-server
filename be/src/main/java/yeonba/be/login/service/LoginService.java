package yeonba.be.login.service;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.login.dto.request.UserLoginRequest;
import yeonba.be.login.dto.request.UserRefreshJwtRequest;
import yeonba.be.login.dto.response.UserLoginResponse;
import yeonba.be.login.dto.response.UserRefrehJwtResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.enums.LoginType;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserQuery userQuery;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {

        User user = userQuery.findByPhoneNumber(request.getPhoneNumber());
        LoginType loginType = LoginType.from(request.getLoginType());
        validateLoginInfo(user, request.getSocialId(), loginType);

        Date now = new Date();
        String jwt = jwtUtil.generateAccessToken(user, now);
        String refreshToken = jwtUtil.generateRefreshToken(user, now);

        // 사용자 refresh token 업데이트
        user.updateRefreshToken(refreshToken);

        return new UserLoginResponse(jwt, refreshToken);
    }

    private void validateLoginInfo(User user, long socialId, LoginType loginType) {

        if (user.getSocialId() != socialId || user.getLoginType() != loginType) {

            throw new GeneralException(UserException.NOT_MATCH_LOGIN_TYPE);
        }
    }

    @Transactional
    public UserRefrehJwtResponse refreshJwt(UserRefreshJwtRequest request) {

        long userId = jwtUtil.getUserIdFromToken(request.getRefreshToken());

        User user = userQuery.findById(userId);
        user.validateRefreshToken(request.getRefreshToken());

        Date now = new Date();
        String jwt = jwtUtil.generateAccessToken(user, now);
        String refreshToken = jwtUtil.generateRefreshToken(user, now);

        user.updateRefreshToken(refreshToken);

        return new UserRefrehJwtResponse(jwt, refreshToken);
    }
}
