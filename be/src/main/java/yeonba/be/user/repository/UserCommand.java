package yeonba.be.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserCommand {

  private final UserRepository userRepository;

  public User save(User user) {

    return userRepository.save(user);
  }
}
