package yeonba.be.login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yeonba.be.login.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

	Optional<VerificationCode> findByPhoneNumberAndCode(String phoneNumber, String code);
}
