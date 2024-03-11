package yeonba.be.arrow.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.dto.UserArrowsResponse;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.arrow.repository.ArrowCommand;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class ArrowService {

  private final int DAILY_CHECK_ARROW_COUNT = 10;
  private final UserQuery userQuery;
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

    User dailyCheckUser = userQuery.findById(userId);

    LocalDateTime dailyCheckedAt = LocalDateTime.now();
    dailyCheckUser.validateDailyCheck(dailyCheckedAt.toLocalDate());

    ArrowTransaction arrowTransaction = new ArrowTransaction(
        dailyCheckUser,
        DAILY_CHECK_ARROW_COUNT);
    arrowCommand.save(arrowTransaction);

    dailyCheckUser.updateLastAccessedAt(dailyCheckedAt);
    dailyCheckUser.addArrow(DAILY_CHECK_ARROW_COUNT);
  }

  @Transactional(readOnly = true)
  public UserArrowsResponse getUserArrows(long userId) {

    User user = userQuery.findById(userId);

    return new UserArrowsResponse(user.getArrow());
  }
}
