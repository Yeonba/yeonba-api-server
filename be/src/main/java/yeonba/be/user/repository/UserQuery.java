package yeonba.be.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserQuery {

	private final UserRepository userRepository;

	public User findById(long userId) {

		return userRepository.findByIdAndDeletedAtIsNull(userId)
			.orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
	}

	public User findByEmail(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(() -> new GeneralException(UserException.USER_NOT_FOUND));
	}
}
