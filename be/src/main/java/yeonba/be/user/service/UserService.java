package yeonba.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(long userId) {

        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
