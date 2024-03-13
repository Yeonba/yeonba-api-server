package yeonba.be.arrow.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class ArrowQuery {

  private final ArrowTransactionRepository arrowTransactionRepository;

  public boolean isArrowTransactionExist(User sentUser, User receivedUser) {

    return arrowTransactionRepository
        .existsBySentUserAndReceivedUser(sentUser, receivedUser);
  }
}
