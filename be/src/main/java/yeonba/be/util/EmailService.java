package yeonba.be.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  @Value("{spring.mail.username}")
  private String serviceEmail;

  private final JavaMailSender mailSender;

  public void sendMail(
      String to,
      String subject,
      String text){
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(serviceEmail);
    mailMessage.setTo(to);
    mailMessage.setSubject(subject);
    mailMessage.setText(text);

    mailSender.send(mailMessage);
  }
}
