package yeonba.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.BlockException;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.BlockCommand;
import yeonba.be.user.repository.BlockQuery;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class BlockService {

  private final BlockQuery blockQuery;
  private final BlockCommand blockCommand;
  private final UserQuery userQuery;

  /*
    차단하기는 다음 과정을 거친다.
    1. 자기 자신을 차단하는 예외 상황 검증
    2. 이미 차단한 사용자인지 검증
    3. 차단 엔티티 생성 및 저장
   */
  @Transactional
  public void block(long userId, long blockedUserId) {

    User user = userQuery.findById(userId);
    User blockedUser = userQuery.findById(blockedUserId);

    user.validateNotSameUser(blockedUser);

    if (blockQuery.isBlockExist(user, blockedUser)) {
      throw new GeneralException(BlockException.ALREADY_BLOCKED_USER);
    }

    Block block = new Block(user, blockedUser);
    blockCommand.save(block);
  }
}
