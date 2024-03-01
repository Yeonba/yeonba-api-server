package yeonba.be.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yeonba.be.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
