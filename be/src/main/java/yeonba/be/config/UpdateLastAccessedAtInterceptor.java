package yeonba.be.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.LoginException;
import yeonba.be.user.service.UserService;

@Component
@RequiredArgsConstructor
public class UpdateLastAccessedAtInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public void afterCompletion(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex) throws Exception {

        Optional<Long> userId = Optional.ofNullable((Long) request.getAttribute("userId"));
        if (userId.isEmpty()) {
            throw new GeneralException(LoginException.UNAUTHORIZED);
        }

        LocalDateTime accessAt = LocalDateTime.now();
        userService.updateLastAccessedAt(userId.get(), accessAt);
    }
}
