package yeonba.be.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import yeonba.be.user.entity.User;
import yeonba.be.user.service.UserService;

@Component
@RequiredArgsConstructor
public class DevAuthInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = userService.findById(2L);
        request.setAttribute("user", user);

        return true;
    }


}
