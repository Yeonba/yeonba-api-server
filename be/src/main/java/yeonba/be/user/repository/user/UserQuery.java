package yeonba.be.user.repository.user;


import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.dto.request.UserSearchRequest;
import yeonba.be.user.dto.response.UserQueryPageResponse;
import yeonba.be.user.dto.response.UserQueryResponse;
import yeonba.be.user.entity.User;

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

    public boolean validateExistsById(long userId) {

        return userRepository.existsById(userId);
    }

    public boolean validateUsedNickname(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    public boolean validateUsedPhoneNumber(String phoneNumber) {

        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    public UserQueryPageResponse findFavoritesBy(long userId, PageRequest pageRequest) {

        Page<UserQueryResponse> page = userRepository.findFavoritesBy(userId, pageRequest);

        return UserQueryPageResponse.from(page);
    }

    public UserQueryPageResponse findArrowReceiversBy(long senderId, PageRequest pageRequest) {

        Page<UserQueryResponse> page = userRepository.findArrowReceiversBy(senderId, pageRequest);

        return UserQueryPageResponse.from(page);
    }

    public UserQueryPageResponse findArrowSendersBy(long receiverId, PageRequest pageRequest) {

        Page<UserQueryResponse> page = userRepository.findArrowSendersBy(receiverId, pageRequest);

        return UserQueryPageResponse.from(page);
    }

    public List<User> findByIds(List<Long> userIds) {

        return userRepository.findAllById(userIds);
    }

    public UserQueryPageResponse findRecommendUsers(
        long userId, boolean userGender, PageRequest pageRequest, LocalDate recommendDay) {

        Page<UserQueryResponse> page = userRepository
            .findRecommendUsers(userId, userGender, pageRequest, recommendDay);

        return UserQueryPageResponse.from(page);
    }

    public UserQueryPageResponse findAllBySearchCondition(
        long userId,
        PageRequest pageRequest,
        LocalDate searchDate,
        UserSearchRequest request) {

        Page<UserQueryResponse> page = userRepository
            .findAllBySearchCondition(userId, pageRequest, searchDate, request);

        return UserQueryPageResponse.from(page);
    }
}
