package yeonba.be.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Component
@RequiredArgsConstructor
public class DevAuthInterceptor implements HandlerInterceptor {

    private final UserQuery userQuery;

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response,
        Object handler) throws Exception {

        long devUserId = 2L;
        User user = userQuery.findById(2L);
        request.setAttribute("userId", devUserId);

        return true;
    }


}
