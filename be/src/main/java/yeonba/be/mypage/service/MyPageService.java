package yeonba.be.mypage.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.mypage.dto.request.UserChangePasswordRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.service.UserService;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserService userService;

    @Transactional(readOnly = true)
    public UserSimpleProfileResponse getSimpleProfile(long userId) {

        User user = userService.findById(userId);

        return new UserSimpleProfileResponse(
            user.getName(),
            user.getRepresentativeProfilePhoto(),
            user.getArrow()
        );
    }

    @Transactional(readOnly = true)
    public UserProfileDetailResponse getProfileDetail(long userId) {

        User user = userService.findById(userId);

        return new UserProfileDetailResponse(user);
    }

    @Transactional
    public void updateProfile(UserUpdateProfileRequest request, long userId) {

        User validatedUser = userService.findById(userId);

        // TODO: 선호 조건 테이블 생성 후 로직 추가

        // validatedUser.updateProfile(request);
    }

    @Transactional
    public void changePassword(UserChangePasswordRequest request, long userId) {

        User user = userService.findById(userId);

        // TODO: 비밀번호 암호화 후 비교
        String encryptedOldPassword = request.getOldPassword();

        if (!StringUtils.equals(user.getEncryptedPassword(), encryptedOldPassword)) {
            throw new IllegalArgumentException("기존 비밀번호가 틀렸습니다.");
        }

        if (!StringUtils.equals(request.getNewPassword(), request.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인 값이 일치하지 않습니다.");
        }

        // TODO: 암호화 후 비밀번호 변경
        String encryptedNewPassword = request.getNewPassword();

        user.changePassword(encryptedNewPassword);
    }
}
