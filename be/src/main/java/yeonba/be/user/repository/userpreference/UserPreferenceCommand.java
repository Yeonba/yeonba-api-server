package yeonba.be.user.repository.userpreference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.UserPreference;

@Component
@RequiredArgsConstructor
public class UserPreferenceCommand {

	private final UserPreferenceRepository userPreferenceRepository;

	public UserPreference save(UserPreference userPreference) {

		return userPreferenceRepository.save(userPreference);
	}
}
