package yeonba.be.user.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yeonba.be.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);

}
