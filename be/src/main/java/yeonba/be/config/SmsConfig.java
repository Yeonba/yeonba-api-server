package yeonba.be.config;

import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

  @Value("${spring.sms.api_key}")
  private String apiKey;

  @Value("${spring.sms.api_secret}")
  private String apiSecret;

  @Value("${spring.sms.provider}")
  private String provider;

  @Bean
  public DefaultMessageService defaultMessageService() {

    return new DefaultMessageService(apiKey, apiSecret, provider);
  }
}
