package yeonba.be.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.login.dto.request.UserPasswordInquiryRequest;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;
import yeonba.be.util.EmailService;
import yeonba.be.util.TemporaryPasswordGenerator;

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

	private final PasswordEncryptor passwordEncryptor;
	private final JwtUtil jwtUtil;
  /*
  임시 비밀번호는 다음 과정을 거친다.
    1. 요청 이메일 기반 사용자 조회
    2. 임시 비밀번호 생성
    3. 사용자 비밀번호, 임시 비밀번호로 변경
    4. 임시 비밀번호 발급 메일 전송
   */

	@Transactional
	public void sendTemporaryPasswordMail(UserPasswordInquiryRequest request) {

		String email = request.getEmail();
		User user = userQuery.findByEmail(email);

		String temporaryPassword = TemporaryPasswordGenerator.generatePassword();

		String encryptedPassword = passwordEncryptor.encrypt(temporaryPassword, user.getSalt());
		user.changePassword(encryptedPassword);

		String text = String.format(TEMPORARY_PASSWORD_EMAIL_TEXT, temporaryPassword);
		emailService.sendMail(email, TEMPORARY_PASSWORD_EMAIL_SUBJECT, text);
	}

	@Transactional
	public void sendVerificationCodeMessage(UserVerificationCodeRequest request) {

		// 전화 번호로 사용자 조회
		String phoneNumber = request.getPhoneNumber();
		if (!userQuery.existByPhoneNumber(phoneNumber)) {
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

		// 핸드폰 번호 기반 사용자 조회 및 인증 코드 내역 삭제
		User user = userQuery.findByPhoneNumber(phoneNumber);
		verificationCodeCommand.delete(verificationCode);

		return new UserEmailInquiryResponse(user.getEmail());
	}

	@Transactional
	public UserLoginResponse login(UserLoginRequest request) {

		// 이메일로 사용자 조회
		String email = request.getEmail();
		User user = userQuery.findByEmail(email);

		// 비밀번호 요청값 암호화 및 저장된 비밀번호와 일치 확인
		String requestedPassword = passwordEncryptor
			.encrypt(request.getPassword(), user.getSalt());
		if (!StringUtils.equals(requestedPassword, user.getEncryptedPassword())) {
			throw new GeneralException(LoginException.PASSWORD_NOT_MATCH);
		}

		// access token, refresh token 발급
		Date issuedAt = new Date();
		String accessToken = jwtUtil.generateAccessToken(user, issuedAt);
		String refreshToken = jwtUtil.generateRefreshToken(user, issuedAt);

		// 사용자 refresh token 업데이트
		user.updateRefreshToken(refreshToken);

		return new UserLoginResponse(accessToken, refreshToken);
	}

	@Transactional(readOnly = true)
	public UserRefreshTokenResponse refreshAccessToken(UserRefreshTokenRequest request) {

		// refresh token에서 userId 파싱
		String refreshToken = request.getRefreshToken();
		long userId = jwtUtil.parseUserIdFromJwt(refreshToken);

		// 사용자 조회 및 refresh token 입력값과 사용자 refresh token 일치 확인
		User user = userQuery.findById(userId);
		if (!StringUtils.equals(refreshToken, user.getRefreshToken())) {
			throw new GeneralException(LoginException.REFRESH_TOKEN_NOT_MATCH);
		}

		// access token 재발급
		String accessToken = jwtUtil.generateAccessToken(user, new Date());

		return new UserRefreshTokenResponse(accessToken);
	}
}
