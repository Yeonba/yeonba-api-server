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

  @Transactional
  public void dailyCheck(User user){

    User dailyCheckUser = userService.findById(user.getId());

    LocalDateTime dailyCheckedAt = LocalDateTime.now();
    if(isAlreadyCheckedUser(dailyCheckUser, dailyCheckedAt.toLocalDate())){
      throw new IllegalArgumentException("이미 출석 체크한 사용자입니다.");
    }

    ArrowTransaction arrowTransaction = new ArrowTransaction(null, dailyCheckUser);
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
