package yeonba.be.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

  boolean existsByUserAndBlockedUser(User user, User blockedUser);
}
