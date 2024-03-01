package yeonba.be.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final DevAuthInterceptor devAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(devAuthInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/swagger-ui/**",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/webjars/**",
                "/error");
    }

}
