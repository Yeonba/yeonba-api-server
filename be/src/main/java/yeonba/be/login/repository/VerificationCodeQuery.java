package yeonba.be.login.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.LoginException;
import yeonba.be.login.entity.VerificationCode;

@Component
@RequiredArgsConstructor
public class VerificationCodeQuery {

	private final VerificationCodeRepository verificationCodeRepository;

	public VerificationCode findBy(String phoneNumber, String code) {

		return verificationCodeRepository.findByPhoneNumberAndCode(phoneNumber, code)
			.orElseThrow(() -> new GeneralException(LoginException.VERIFICATION_CODE_NOT_FOUND));
	}
}
