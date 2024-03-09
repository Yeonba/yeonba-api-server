package yeonba.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.ExceptionType;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.BlockCommand;
import yeonba.be.user.repository.BlockQuery;

@Service
@RequiredArgsConstructor
public class BlockService {

  private final BlockQuery blockQuery;
  private final BlockCommand blockCommand;
  private final UserService userService;

  /*
    차단하기는 다음 과정을 거친다.
    1. 자기 자신을 차단하는 예외 상황 검증
    2. 이미 차단한 사용자인지 검증
    3. 차단 엔티티 생성 및 저장
   */
  @Transactional
  public void block(long userId, long blockedUserId) {

    if (userId == blockedUserId) {
      throw new GeneralException(ExceptionType.CAN_NOT_BLOCK_SELF);
    }

    User user = userService.findById(userId);
    User blockedUser = userService.findById(blockedUserId);

    if (isAlreadyBlockedUser(user, blockedUser)) {
      throw new GeneralException(ExceptionType.ALREADY_BLOCKED_USER);
    }

    Block block = new Block(user, blockedUser);
    blockCommand.save(block);
  }

  private boolean isAlreadyBlockedUser(User user, User blockedUser) {

    return blockQuery.existsByUserAndBlockedUser(user, blockedUser);
  }
}
