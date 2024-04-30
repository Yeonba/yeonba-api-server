package yeonba.be.user.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndDeletedIsFalse(long userId);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndDeletedIsFalse(String phoneNumber);

    boolean existsByNickname(String nickname);
}
