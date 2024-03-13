package yeonba.be.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeonba.be.login.dto.request.UserPasswordInquiryRequest;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;
import yeonba.be.util.EmailService;
import yeonba.be.util.TemporaryPasswordGenerator;

@Service
@RequiredArgsConstructor
public class LoginService {

  private final String TEMPORARY_PASSWORD_EMAIL_SUBJECT = "연바(연애는 바로 지금) 임시비밀번호 발급";
  private final String TEMPORARY_PASSWORD_EMAIL_TEXT = "임시비밀번호 : %s";
  private final UserQuery userQuery;
  private final EmailService emailService;

  /*
  임시 비밀번호는 다음 과정을 거친다.
    1. 요청 이메일 기반 사용자 조회
    2. 임시 비밀번호 생성
    3. 사용자 비밀번호, 임시 비밀번호로 변경
    4. 임시 비밀번호 발급 메일 전송
   */

  // TODO : 비밀번호 암호화 로직 추가
  public void sendTemporaryPasswordMail(UserPasswordInquiryRequest request) {

    String email = request.getEmail();
    User user = userQuery.findByEmail(email);

    String temporaryPassword = TemporaryPasswordGenerator.generatePassword();

    String encryptedPassword = temporaryPassword;
    user.changePassword(encryptedPassword);

    String text = String.format(TEMPORARY_PASSWORD_EMAIL_TEXT, temporaryPassword);
    emailService.sendMail(email, TEMPORARY_PASSWORD_EMAIL_SUBJECT, text);
  }
}
