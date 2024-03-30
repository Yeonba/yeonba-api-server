package yeonba.be.user.repository.userrecommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.UserRecommendation;

@Component
@RequiredArgsConstructor
public class UserRecommendationCommand {

    private final UserRecommendationRepository userRecommendationRepository;

    public UserRecommendation save(UserRecommendation userRecommendation) {

        return userRecommendationRepository.save(userRecommendation);
    }
}
