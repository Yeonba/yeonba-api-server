package yeonba.be.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
