package yeonba.be.user.repository.usersearchlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.UserSearchLog;

@Repository
public interface UserSearchLogRepository extends JpaRepository<UserSearchLog, Long> {

}
