package yeonba.be.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.ExceptionType;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserQuery {

  private final UserRepository userRepository;

  public User findById(long userId) {

    return userRepository.findById(userId)
        .orElseThrow(() -> new GeneralException(ExceptionType.USER_NOT_FOUND));
  }

}
