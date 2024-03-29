package yeonba.be.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import yeonba.be.user.dto.response.UserQueryResponse;

public interface UserRepositoryCustom {

    Page<UserQueryResponse> findAllFavorites(long userId, PageRequest pageRequest);

    Page<UserQueryResponse> findAllArrowReceivers(long senderId, PageRequest pageRequest);
}