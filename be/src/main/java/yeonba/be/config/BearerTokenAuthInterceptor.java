package yeonba.be.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.LoginException;
import yeonba.be.util.JwtUtil;
import yeonba.be.util.ServiceRegex;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    /*
    access token을 이용한 인가는 이하 과정을 거쳐 이뤄진다.
    1. Authorization 헤더 값이 존재하는 지 확인
    2. Bearer 토큰 형식이 맞는 지 확인
    3. jwt 검증(형식, 시그니처, 만료 여부)
    4. access token 파싱, userId 파싱
    5. userId request attribute 설정
     */
    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler) throws Exception {

        // authorization 헤더 값 존재여부 확인
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.hasText(bearerToken)) {
            throw new GeneralException(LoginException.ACCESS_TOKEN_NOT_EXIST);
        }

        // Bearer 토큰 형식 확인
        String regex = ServiceRegex.BEARER_TOKEN.getPattern();
        Matcher matcher = Pattern.compile(regex).matcher(bearerToken);
        if (!matcher.find()) {
            throw new GeneralException(LoginException.INVALID_BEARER_TOKEN_FORMAT);
        }

        // access token, userId 파싱 및 userId request attribute 설정
        String accessToken = matcher.group(1);
        long userId = jwtUtil.parseUserIdFromJwt(accessToken);
        request.setAttribute("userId", userId);

        return true;
    }
}
