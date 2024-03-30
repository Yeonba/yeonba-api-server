package yeonba.be.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.dto.response.UserQueryPageResponse;
import yeonba.be.user.dto.response.UserQueryResponse;
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

    public boolean isUserExist(String phoneNumber) {

        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User findByPhoneNumber(String phoneNumber) {

        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
    }

    public boolean isAlreadyUsedNickname(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    public boolean isAlreadyUsedEmail(String email) {

        return userRepository.existsByEmail(email);
    }

    public UserQueryPageResponse findAllFavorites(long userId, PageRequest pageRequest) {

        Page<UserQueryResponse> page = userRepository.findAllFavorites(userId, pageRequest);

        return UserQueryPageResponse.from(page);
    }

    public UserQueryPageResponse findAllArrowReceivers(long senderId, PageRequest pageRequest) {

        Page<UserQueryResponse> page = userRepository.findAllArrowReceivers(senderId, pageRequest);

        return UserQueryPageResponse.from(page);
    }
}
