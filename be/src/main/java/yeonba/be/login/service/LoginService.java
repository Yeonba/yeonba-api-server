package yeonba.be.login.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.JoinException;
import yeonba.be.exception.UserException;
import yeonba.be.login.dto.request.UserEmailInquiryRequest;
import yeonba.be.login.dto.request.UserLoginRequest;
import yeonba.be.login.dto.request.UserPasswordInquiryRequest;
import yeonba.be.login.dto.request.UserVerificationCodeRequest;
import yeonba.be.login.dto.request.UserVerifyPhoneNumberRequest;
import yeonba.be.login.dto.response.UserEmailInquiryResponse;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.login.dto.response.UserLoginResponse;
import yeonba.be.login.entity.VerificationCode;
import yeonba.be.login.repository.VerificationCodeCommand;
import yeonba.be.login.repository.VerificationCodeQuery;
import yeonba.be.user.entity.User;
import yeonba.be.user.enums.LoginType;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.util.EmailService;
import yeonba.be.util.JwtUtil;
import yeonba.be.util.PasswordEncryptor;
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

    private final PasswordEncryptor passwordEncryptor;
    private final JwtUtil jwtUtil;

    public UserLoginResponse login(UserLoginRequest request) {

        User user = userQuery.findBySocialIdAndLoginType(
            request.getSocialId(), LoginType.from(request.getLoginType()));

        Date now = new Date();
        String accessToken = jwtUtil.generateAccessToken(user, now);
        String refreshToken = jwtUtil.generateRefreshToken(user, now);

        // 사용자 refresh token 업데이트
        user.updateRefreshToken(refreshToken);

        return new UserLoginResponse(accessToken, refreshToken);
    }

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

        // 해당 번호를 가진 사용자가 존재하는 지 확인
        String phoneNumber = request.getPhoneNumber();
        if (!userQuery.validateUsedPhoneNumber(phoneNumber)) {

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
        LocalDateTime verifyAt = LocalDateTime.now();

        // 인증 코드 조회
        VerificationCode verificationCode = verificationCodeQuery
            .findBy(phoneNumber, code, verifyAt);

        // 핸드폰 번호 기반 사용자 조회 및 인증 코드 내역 삭제
        User user = userQuery.findByPhoneNumber(phoneNumber);
        verificationCodeCommand.delete(verificationCode);

        return new UserEmailInquiryResponse(user.getEmail());
    }

    @Transactional
    public void sendJoinVerificationCodeMessage(UserVerificationCodeRequest request) {

        // 이미 사용 중인 번호인 지 검증
        if (userQuery.validateUsedPhoneNumber(request.getPhoneNumber())) {

            throw new GeneralException(JoinException.ALREADY_USED_PHONE_NUMBER);
        }

        // 인증 코드 생성 및 저장
        VerificationCode verificationCode = saveVerificationCode(request);

        // 인증 코드 메시지 전송
        String message = String.format(VERIFICATION_CODE_MESSAGE, verificationCode.getCode());
        smsService.sendMessage(request.getPhoneNumber(), message);
    }

    private VerificationCode saveVerificationCode(UserVerificationCodeRequest request) {

        String phoneNumber = request.getPhoneNumber();
        String code = VerificationCodeGenerator.generateVerificationCode();
        LocalDateTime expiredAt = LocalDateTime.now()
            .plus(VERIFICATION_CODE_TTL, ChronoUnit.MINUTES);
        VerificationCode verificationCode = new VerificationCode(phoneNumber, code, expiredAt);

        return verificationCodeCommand.save(verificationCode);
    }

    @Transactional
    public void verifyPhoneNumber(UserVerifyPhoneNumberRequest request) {

        String code = request.getVerificationCode();
        LocalDateTime verifyAt = LocalDateTime.now();

        VerificationCode verificationCode = verificationCodeQuery
            .findBy(request.getPhoneNumber(), code, verifyAt);

        verificationCodeCommand.delete(verificationCode);
    }

    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void deleteExpiredVerificationCodes() {

        LocalDateTime deletedAt = LocalDateTime.now();
        verificationCodeCommand.deleteAllExpiredAtBefore(deletedAt);
    }
}
