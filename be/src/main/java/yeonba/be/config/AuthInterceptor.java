package yeonba.be.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UtilException;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final UserQuery userQuery;
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (header == null) {
            throw new GeneralException(UtilException.NOT_FOUND_AUTHORIZATION_HEADER);
        }

        Pattern pattern = Pattern.compile("^Bearer\\s(.+)$");
        Matcher matcher = pattern.matcher(header);
        if (!matcher.matches()) {
            throw new GeneralException(UtilException.INVALID_AUTHORIZATION_FORMAT);
        }

        String token = matcher.group(1);



        if (!jwtUtil.validateTokenIsExpired(token)) {
            throw new GeneralException(UtilException.INVALID_JWT);
        }

        if (!jwtUtil.validateTokenIsManipulated(token)) {
            throw new GeneralException(UtilException.INVALID_JWT);
        }

        long userId = jwtUtil.getUserIdFromJwt(token);
        request.setAttribute("userId", userId);

        return true;
    }
}
