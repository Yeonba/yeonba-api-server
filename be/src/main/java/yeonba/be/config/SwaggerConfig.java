package yeonba.be.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Info info = new Info()
            .title("Yeonba API Docs")
            .description("연바 API 명세서입니다.")
            .description("<h3> Yeonba API</h3><img src=\"https://avatars.githubusercontent.com/u/156646513?s=200&v=4\" width=150 height=150 />")
            .version("0.0.1");

        return new OpenAPI()
            .components(new Components())
            .info(info);
    }


}
