package yeonba.be.config;

import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

  @Value("${SMS_API_KEY}")
  private String apiKey;

	@Value("${SMS_API_SECRET}")
  private String apiSecret;

	@Value("${SMS_PROVIDER}")
  private String provider;

  @Bean
  public DefaultMessageService defaultMessageService() {

    return new DefaultMessageService(apiKey, apiSecret, provider);
  }
}
