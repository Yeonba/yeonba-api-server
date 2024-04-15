package yeonba.be.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

  Optional<Block> findByUserAndBlockedUser(User user, User blockedUser);

  boolean existsByUserAndBlockedUser(User user, User blockedUser);

  List<Block> findByUser(User user);
}
