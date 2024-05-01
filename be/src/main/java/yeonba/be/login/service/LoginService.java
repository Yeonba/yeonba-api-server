package yeonba.be.login.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.JoinException;
import yeonba.be.exception.UserException;
import yeonba.be.login.dto.request.UserLoginRequest;
import yeonba.be.login.dto.request.UserRefreshJwtRequest;
import yeonba.be.login.dto.request.UserVerificationCodeRequest;
import yeonba.be.login.dto.request.UserVerifyPhoneNumberRequest;
import yeonba.be.login.dto.response.UserLoginResponse;
import yeonba.be.login.dto.response.UserRefrehJwtResponse;
import yeonba.be.login.entity.VerificationCode;
import yeonba.be.login.repository.VerificationCodeCommand;
import yeonba.be.login.repository.VerificationCodeQuery;
import yeonba.be.user.entity.User;
import yeonba.be.user.enums.LoginType;
import yeonba.be.user.repository.user.UserQuery;
import yeonba.be.util.JwtUtil;
import yeonba.be.util.SmsService;
import yeonba.be.util.VerificationCodeGenerator;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final long VERIFICATION_CODE_TTL = 5;

    private final String VERIFICATION_CODE_MESSAGE = "연바(연애는 바로 지금) 인증 코드 : %s";

    private final UserQuery userQuery;
    private final VerificationCodeCommand verificationCodeCommand;
    private final VerificationCodeQuery verificationCodeQuery;

    private final SmsService smsService;

    private final JwtUtil jwtUtil;

    @Transactional
    public UserLoginResponse login(UserLoginRequest request) {

        User user = userQuery.findByPhoneNumber(request.getPhoneNumber());
        LoginType loginType = LoginType.from(request.getLoginType());
        validateLoginInfo(user, request.getSocialId(), loginType);

        Date now = new Date();
        String jwt = jwtUtil.generateAccessToken(user, now);
        String refreshToken = jwtUtil.generateRefreshToken(user, now);

        // 사용자 refresh token 업데이트
        user.updateRefreshToken(refreshToken);

        return new UserLoginResponse(jwt, refreshToken);
    }

    private void validateLoginInfo(User user, long socialId, LoginType loginType) {

        if (user.getSocialId() != socialId || user.getLoginType() != loginType) {

            throw new GeneralException(UserException.NOT_MATCH_LOGIN_TYPE);
        }
    }

    @Transactional
    public UserRefrehJwtResponse refreshJwt(UserRefreshJwtRequest request) {

        long userId = jwtUtil.getUserIdFromToken(request.getRefreshToken());

        User user = userQuery.findById(userId);
        user.validateRefreshToken(request.getRefreshToken());

        Date now = new Date();
        String jwt = jwtUtil.generateAccessToken(user, now);
        String refreshToken = jwtUtil.generateRefreshToken(user, now);

        user.updateRefreshToken(refreshToken);

        return new UserRefrehJwtResponse(jwt, refreshToken);
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
