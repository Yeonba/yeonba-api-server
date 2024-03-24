package yeonba.be.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Value("${GOOGLE_SMTP_HOST}")
	private String serverHost;

	@Value("${GOOGLE_SMTP_PORT}")
	private int serverPort;

	@Value("${GOOGLE_SMTP_USERNAME}")
	private String username;

	@Value("${GOOGLE_SMTP_PASSWORD}")
	private String password;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(serverHost);
		mailSender.setPort(serverPort);
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		Properties properties = mailSender.getJavaMailProperties();
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		return mailSender;
	}
}
