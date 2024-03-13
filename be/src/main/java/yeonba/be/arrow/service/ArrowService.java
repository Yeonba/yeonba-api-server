package yeonba.be.arrow.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.dto.UserArrowsResponse;
import yeonba.be.arrow.dto.request.ArrowSendRequest;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.arrow.repository.ArrowCommand;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.exception.ArrowException;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class ArrowService {

  private final int DAILY_CHECK_ARROW_COUNT = 10;
  private final UserQuery userQuery;
  private final ArrowCommand arrowCommand;
  private final ArrowQuery arrowQuery;

  /*
    출석 체크는 다음 과정을 거쳐 이뤄진다.
    1. 사용자 최종 접속 일시를 통해 이미 출석 체크하였는지 확인
    2. 화살 송수신 내역 저장
    3. 사용자 최종 접속 일시 갱신
    4. 사용자 화살 개수 증가
   */

  @Transactional
  public void dailyCheck(long userId) {

    User dailyCheckUser = userQuery.findById(userId);

    LocalDateTime dailyCheckedAt = LocalDateTime.now();
    dailyCheckUser.validateDailyCheck(dailyCheckedAt.toLocalDate());

    ArrowTransaction arrowTransaction = new ArrowTransaction(
        dailyCheckUser,
        DAILY_CHECK_ARROW_COUNT);
    arrowCommand.save(arrowTransaction);

    dailyCheckUser.updateLastAccessedAt(dailyCheckedAt);
    dailyCheckUser.plusArrow(DAILY_CHECK_ARROW_COUNT);
  }

  @Transactional(readOnly = true)
  public UserArrowsResponse getUserArrows(long userId) {

    User user = userQuery.findById(userId);

    return new UserArrowsResponse(user.getArrow());
  }

  /*
  화살 보내기 비즈니스 로직은 다음 과정을 거친다.
  1. 자기 자신에게 화살을 보내는 상황 검증
  2. 화살을 보낸 사용자에게 또 보내는 상황 검증
  3. 화살 내역 저장
  4. 보내는 사용자 화살 감소, 화살이 부족할 경우 예외 발생
  5. 받는 사용자 화살 증가
 */
  @Transactional
  public void sendArrow(
      long senderId,
      long recipientId,
      ArrowSendRequest request) {

    User sender = userQuery.findById(senderId);
    User receiver = userQuery.findById(recipientId);

    sender.validateNotSameUser(receiver);

    if (arrowQuery.isArrowTransactionExist(sender, receiver)) {
      throw new GeneralException(ArrowException.ALREADY_SENT_ARROW_USER);
    }

    int arrows = request.getArrows();
    ArrowTransaction arrowTransaction = new ArrowTransaction(
        sender,
        receiver,
        arrows);
    arrowCommand.save(arrowTransaction);

    sender.minusArrow(arrows);
    receiver.plusArrow(arrows);
  }
}
