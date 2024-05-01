package yeonba.be.user.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.User;
import yeonba.be.user.enums.LoginType;

@Component
@RequiredArgsConstructor
public class UserQuery {

    private final UserRepository userRepository;

    public User findById(long userId) {

        return userRepository.findByIdAndDeletedIsFalse(userId)
            .orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
    }

    public User findByPhoneNumber(String phoneNumber) {

        return userRepository.findByPhoneNumberAndDeletedIsFalse(phoneNumber)
            .orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
    }

    public boolean validateUsedNickname(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    public boolean validateUsedPhoneNumber(String phoneNumber) {

        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
