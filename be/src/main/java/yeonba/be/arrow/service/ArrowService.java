package yeonba.be.arrow.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.dto.request.ArrowSendRequest;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.arrow.repository.ArrowCommand;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.arrow.repository.ArrowTransactionCommandRepository;
import yeonba.be.exception.ExceptionType;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.User;
import yeonba.be.user.service.UserService;

@Service
@RequiredArgsConstructor
public class ArrowService {

  private final int DAILY_CHECK_ARROW_COUNT = 10;
  private final UserService userService;
  private final ArrowTransactionCommandRepository arrowTransactionCommandRepository;
  private final ArrowQuery arrowQuery;
  private final ArrowCommand arrowCommand;

  /*
    출석 체크는 다음 과정을 거쳐 이뤄진다.
    1. 사용자 최종 접속 일시를 통해 이미 출석 체크하였는지 확인
    2. 화살 송수신 내역 저장
    3. 사용자 최종 접속 일시 갱신
    4. 사용자 화살 개수 증가
   */

  @Transactional
  public void dailyCheck(long userId) {

    User dailyCheckUser = userService.findById(userId);

    LocalDateTime dailyCheckedAt = LocalDateTime.now();
    if (isAlreadyCheckedUser(dailyCheckUser, dailyCheckedAt.toLocalDate())) {
      throw new IllegalArgumentException("이미 출석 체크한 사용자입니다.");
    }

    ArrowTransaction arrowTransaction = new ArrowTransaction(
        dailyCheckUser,
        DAILY_CHECK_ARROW_COUNT
    );
    arrowTransactionCommandRepository.save(arrowTransaction);

    dailyCheckUser.updateLastAccessedAt(dailyCheckedAt);
    dailyCheckUser.addArrow(DAILY_CHECK_ARROW_COUNT);
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

    if (senderId == recipientId) {
      throw new GeneralException(ExceptionType.CAN_NOT_SEND_ARROW_SELF);
    }

    User sender = userService.findById(senderId);
    User recipient = userService.findById(recipientId);

    if (isAlreadySentArrowUser(sender, recipient)) {
      throw new GeneralException(ExceptionType.ALREADY_SENT_ARROW_USER);
    }

    int arrows = request.getArrows();
    ArrowTransaction arrowTransaction = new ArrowTransaction(
        sender,
        recipient,
        arrows);
    arrowCommand.save(arrowTransaction);

    sender.minusArrow(arrows);
    recipient.addArrow(arrows);
  }

  private boolean isAlreadyCheckedUser(User user, LocalDate dailyCheckDate) {

    return user.getLastAccessedAt()
        .toLocalDate()
        .equals(dailyCheckDate);
  }

  private boolean isAlreadySentArrowUser(User sender, User recipient) {

    return arrowQuery
        .existsBySentUserAndReceivedUser(sender, recipient);
  }

}
