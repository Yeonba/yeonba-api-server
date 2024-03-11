package yeonba.be.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class BlockQuery {

  private final BlockRepository blockRepository;

  public boolean isBlockExist(User user, User blockedUser) {

    return blockRepository.existsByUserAndBlockedUser(user, blockedUser);
  }
}
