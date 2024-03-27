package yeonba.be.login.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.login.entity.VerificationCode;

@Component
@RequiredArgsConstructor
public class VerificationCodeCommand {

	private final VerificationCodeRepository verificationCodeRepository;

	public VerificationCode save(VerificationCode verificationCode) {

		return verificationCodeRepository.save(verificationCode);
	}

	public void delete(VerificationCode verificationCode) {

		verificationCodeRepository.delete(verificationCode);
	}
}
