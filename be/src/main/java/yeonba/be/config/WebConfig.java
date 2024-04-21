package yeonba.be.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final DevAuthInterceptor devAuthInterceptor;
    private final UpdateLastAccessedAtInterceptor updateLastAccessedAtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(devAuthInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/swagger-ui/**",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/webjars/**",
                "/error")
            .excludePathPatterns(
                "/users/join/**",
                "/users/email-inquiry/**",
                "/users/pw-inquiry",
                "/users/login",
                "/users/refresh");

        registry.addInterceptor(updateLastAccessedAtInterceptor)
            .addPathPatterns("/daily-check");
    }
}
