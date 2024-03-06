package yeonba.be.mypage.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.mypage.dto.request.UserChangePasswordRequest;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.service.UserService;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserService userService;

    @Transactional(readOnly = true)
    public UserSimpleProfileResponse getSimpleProfile(User user) {

        return new UserSimpleProfileResponse(
            user.getName(),
            user.getRepresentativeProfilePhoto(),
            user.getArrow()
        );
    }

    @Transactional
    public void changePassword(UserChangePasswordRequest request, User user) {

        // TODO: 비밀번호 암호화 후 비교
        String encryptedOldPassword = request.getOldPassword();

        if (!StringUtils.equals(user.getEncryptedPassword(), encryptedOldPassword)) {
            throw new IllegalArgumentException("기존 비밀번호가 틀렸습니다.");
        }

        if (!StringUtils.equals(request.getNewPassword(), request.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인 값이 일치하지 않습니다.");
        }

        User validatedUser = userService.findById(user.getId());

        // TODO: 암호화 후 비밀번호 변경
        String encryptedNewPassword = request.getNewPassword();

        validatedUser.changePassword(encryptedNewPassword);
    }
}
