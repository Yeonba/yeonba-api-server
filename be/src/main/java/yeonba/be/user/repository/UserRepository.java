package yeonba.be.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	List<User> findAllByDeletedAtIsBeforeAndDeletedIsFalse(LocalDateTime now);
}
