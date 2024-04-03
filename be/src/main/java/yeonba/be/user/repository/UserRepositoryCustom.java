package yeonba.be.user.repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import yeonba.be.user.dto.request.UserSearchRequest;
import yeonba.be.user.dto.response.UserQueryResponse;

public interface UserRepositoryCustom {

    Page<UserQueryResponse> findAllFavorites(long userId, PageRequest pageRequest);

    Page<UserQueryResponse> findAllArrowReceivers(long senderId, PageRequest pageRequest);

    Page<UserQueryResponse> findAllArrowSenders(long receiverId, PageRequest pageRequest);

    Page<UserQueryResponse> findRecommendUsers(
        long userId,
        PageRequest pageRequest,
        LocalDate recommendDate);

    Page<UserQueryResponse> findAllBySearchCondition(
        long userId,
        PageRequest pageRequest,
        LocalDate searchDate,
        UserSearchRequest request);
}