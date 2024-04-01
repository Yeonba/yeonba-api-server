package yeonba.be.login.repository;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.LoginException;
import yeonba.be.login.entity.VerificationCode;

@Component
@RequiredArgsConstructor
public class VerificationCodeQuery {

    private final VerificationCodeRepository verificationCodeRepository;

    public VerificationCode findNotExpiredVerificationCodeBy(
        String phoneNumber,
        String code,
        LocalDateTime verifyAt) {

        return verificationCodeRepository
            .findFirstByPhoneNumberAndCodeAndExpiredAtIsAfter(phoneNumber, code, verifyAt)
            .orElseThrow(() -> new GeneralException(LoginException.VERIFICATION_CODE_NOT_FOUND));
    }
}
