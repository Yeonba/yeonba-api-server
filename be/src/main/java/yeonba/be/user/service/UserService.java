package yeonba.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserQuery userQuery;

  public User findById(long userId) {

    return userQuery.findById(userId);
  }
}
