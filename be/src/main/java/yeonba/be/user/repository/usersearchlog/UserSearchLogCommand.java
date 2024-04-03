package yeonba.be.user.repository.usersearchlog;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.UserSearchLog;

@Component
@RequiredArgsConstructor
public class UserSearchLogCommand {

    private final UserSearchLogRepository userSearchLogRepository;

    public List<UserSearchLog> saveAll(List<UserSearchLog> userSearchLogs) {

        return userSearchLogRepository.saveAll(userSearchLogs);
    }
}