package yeonba.be.login.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.LoginException;
import yeonba.be.exception.UserException;
import yeonba.be.login.dto.request.UserEmailInquiryRequest;
import yeonba.be.login.dto.request.UserPasswordInquiryRequest;
import yeonba.be.login.dto.request.UserPhoneNumberVerifyRequest;
import yeonba.be.login.dto.response.UserEmailInquiryResponse;
import yeonba.be.login.entity.VerificationCode;
import yeonba.be.login.repository.VerificationCodeCommand;
import yeonba.be.login.repository.VerificationCodeQuery;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;
import yeonba.be.util.EmailService;
import yeonba.be.util.SmsService;
import yeonba.be.util.TemporaryPasswordGenerator;
import yeonba.be.util.VerificationCodeGenerator;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final long VERIFICATION_CODE_TTL = 5;

	private final String TEMPORARY_PASSWORD_EMAIL_SUBJECT = "연바(연애는 바로 지금) 임시비밀번호 발급";
	private final String TEMPORARY_PASSWORD_EMAIL_TEXT = "임시비밀번호 : %s";
	private final String VERIFICATION_CODE_MESSAGE = "연바(연애는 바로 지금) 인증 코드 : %s";

	private final UserQuery userQuery;
	private final VerificationCodeCommand verificationCodeCommand;
	private final VerificationCodeQuery verificationCodeQuery;

	private final EmailService emailService;
	private final SmsService smsService;

  	/*
	임시 비밀번호는 다음 과정을 거친다.
	1. 요청 이메일 기반 사용자 조회
	2. 임시 비밀번호 생성
	3. 사용자 비밀번호, 임시 비밀번호로 변경
	4. 임시 비밀번호 발급 메일 전송
   	*/

	// TODO : 비밀번호 암호화 로직 추가

	@Transactional
	public void sendTemporaryPasswordMail(UserPasswordInquiryRequest request) {

		String email = request.getEmail();
		User user = userQuery.findByEmail(email);

		String temporaryPassword = TemporaryPasswordGenerator.generatePassword();

		String encryptedPassword = temporaryPassword;
		user.changePassword(encryptedPassword);

		String text = String.format(TEMPORARY_PASSWORD_EMAIL_TEXT, temporaryPassword);
		emailService.sendMail(email, TEMPORARY_PASSWORD_EMAIL_SUBJECT, text);
	}

	@Transactional
	public void sendVerificationCodeMessage(UserPhoneNumberVerifyRequest request) {

		// 전화 번호로 사용자 조회
		String phoneNumber = request.getPhoneNumber();
		if (!userQuery.isUserExist(phoneNumber)) {
			throw new GeneralException(UserException.USER_NOT_FOUND);
		}

		// 인증 코드 생성 및 저장
		String code = VerificationCodeGenerator.generateVerificationCode();
		LocalDateTime expiredAt = LocalDateTime.now()
			.plus(VERIFICATION_CODE_TTL, ChronoUnit.MINUTES);
		VerificationCode verificationCode = new VerificationCode(phoneNumber, code, expiredAt);
		verificationCodeCommand.save(verificationCode);

		// 인증 코드 sms 발송
		String message = String.format(VERIFICATION_CODE_MESSAGE, code);
		smsService.sendMessage(phoneNumber, message);
	}

	@Transactional
	public UserEmailInquiryResponse findEmail(UserEmailInquiryRequest request) {

		String phoneNumber = request.getPhoneNumber();
		String code = request.getVerificationCode();

		// 인증 코드 조회
		VerificationCode verificationCode = verificationCodeQuery.findBy(phoneNumber, code);

		// 인증 코드 만료 여부 확인
		if (verificationCode.isExpired(LocalDateTime.now())) {
			throw new GeneralException(LoginException.EXPIRED_VERIFICATION_CODE);
		}

		// 인증 코드 일치 확인
		if (!StringUtils.equals(code, verificationCode.getCode())) {
			throw new GeneralException(LoginException.VERIFICATION_CODE_NOT_MATCH);
		}

		// 핸드폰 번호 기반 사용자 조회 및 인증 코드 내역 삭제
		User user = userQuery.findByPhoneNumber(phoneNumber);
		verificationCodeCommand.delete(verificationCode);

		return new UserEmailInquiryResponse(user.getEmail());
	}
}
