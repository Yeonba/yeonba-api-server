package yeonba.be.user.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.User;
import yeonba.be.user.enums.LoginType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndDeletedIsFalse(long userId);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findBySocialIdAndLoginType(long socialId, LoginType loginType);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);
}
