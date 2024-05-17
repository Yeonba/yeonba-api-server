package yeonba.be.user.repository.user;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import yeonba.be.user.dto.request.UserSearchRequest;
import yeonba.be.user.dto.response.UserQueryResponse;
import yeonba.be.user.entity.User;

public interface UserRepositoryCustom {

    Page<UserQueryResponse> findFavoritesBy(long userId, PageRequest pageRequest);

    Page<UserQueryResponse> findArrowReceiversBy(long senderId, PageRequest pageRequest);

    Page<UserQueryResponse> findArrowSendersBy(long receiverId, PageRequest pageRequest);

    Page<UserQueryResponse> findRecommendUsers(
        long userId,
        boolean userGender,
        PageRequest pageRequest,
        LocalDate recommendDay);

    Page<UserQueryResponse> findUsersBySearchCondition(
        User searchingUser,
        PageRequest pageRequest,
        LocalDate searchDay,
        UserSearchRequest request);
}