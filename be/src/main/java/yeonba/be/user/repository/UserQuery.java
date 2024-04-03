package yeonba.be.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserQuery {

    private final UserRepository userRepository;

    public User findById(long userId) {

        return userRepository.findById(userId)
            .orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
    }

    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
    }

    public boolean isAlreadyUsedPhoneNumber(String phoneNumber) {

        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User findByPhoneNumber(String phoneNumber) {

        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
    }

    public List<User> findWillDeleteUsers() {

        LocalDateTime now = LocalDateTime.now();

        return userRepository.findAllByDeletedAtIsBeforeAndDeletedIsFalse(now);
    }

    public boolean isAlreadyUsedNickname(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    public boolean isAlreadyUsedEmail(String email) {

        return userRepository.existsByEmail(email);
    }
}
