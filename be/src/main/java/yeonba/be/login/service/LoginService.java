package yeonba.be.login.service;

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
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;
import yeonba.be.util.EmailService;
import yeonba.be.util.RedisUtil;
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

	private final EmailService emailService;
	private final SmsService smsService;

	private final RedisUtil redisUtil;

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

	@Transactional(readOnly = true)
	public void sendVerificationCodeMessage(UserPhoneNumberVerifyRequest request) {
		String phoneNumber = request.getPhoneNumber();
		if (!userQuery.isUserExist(phoneNumber)) {
			throw new GeneralException(UserException.USER_NOT_FOUND);
		}

		// 인증 코드 재발급 요청시 기존 발급 내역 삭제
		redisUtil.deleteData(phoneNumber);

		String code = VerificationCodeGenerator.generateVerificationCode();
		redisUtil.putData(phoneNumber, code, VERIFICATION_CODE_TTL);

		String message = String.format(VERIFICATION_CODE_MESSAGE, code);
		smsService.sendMessage(phoneNumber, message);
	}

	@Transactional(readOnly = true)
	public UserEmailInquiryResponse findEmail(UserEmailInquiryRequest request) {
		String phoneNumber = request.getPhoneNumber();
		String verificationCode = request.getVerificationCode();

		// 인증 코드 조회
		String foundVerificationCode = (String) redisUtil.getData(phoneNumber)
			.orElseThrow(() -> new GeneralException(LoginException.VERIFICATION_CODE_NOT_FOUND));

		// 인증 코드 일치 확인
		if (!StringUtils.equals(foundVerificationCode, verificationCode)) {
			throw new GeneralException(LoginException.VERIFICATION_CODE_NOT_MATCH);
		}

		// 핸드폰 번호 기반 사용자 조회 및 인증 코드 내역 삭제
		User user = userQuery.findByPhoneNumber(phoneNumber);
		redisUtil.deleteData(phoneNumber);

		return new UserEmailInquiryResponse(user.getEmail());
	}
}
