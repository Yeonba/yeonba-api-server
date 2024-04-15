package yeonba.be.user.repository.userpreference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;

@Component
@RequiredArgsConstructor
public class UserPreferenceQuery {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreference findByUser(User user) {

        return userPreferenceRepository.findByUser(user)
            .orElseThrow(()->new GeneralException(UserException.USER_PREFERENCE_NOT_FOUND));
    }

}
