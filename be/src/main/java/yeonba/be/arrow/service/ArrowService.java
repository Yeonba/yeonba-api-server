package yeonba.be.arrow.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.arrow.repository.ArrowTransactionCommandRepository;
import yeonba.be.user.entity.User;
import yeonba.be.user.service.UserService;

@Service
@RequiredArgsConstructor
public class ArrowService {

  private final int DAILY_CHECK_ARROW_COUNT = 10;
  private final UserService userService;
  private final ArrowTransactionCommandRepository arrowTransactionCommandRepository;

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
    addUserArrow(dailyCheckUser, DAILY_CHECK_ARROW_COUNT);
  }

  private boolean isAlreadyCheckedUser(User user, LocalDate dailyCheckDate){

    return user.getLastAccessedAt()
        .toLocalDate()
        .equals(dailyCheckDate);
  }

  private void addUserArrow(User user, int arrowCount){

    user.addArrow(arrowCount);
  }
}
