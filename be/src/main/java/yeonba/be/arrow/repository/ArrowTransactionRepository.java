package yeonba.be.arrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.user.entity.User;

@Repository
public interface ArrowTransactionRepository extends JpaRepository<ArrowTransaction, Long>,
    ArrowTransactionRepositoryCustom {

  boolean existsBySentUserAndReceivedUser(User sentUser, User receivedUser);
}
