package yeonba.be.user.repository.userrecommendation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.UserRecommendation;

@Component
@RequiredArgsConstructor
public class UserRecommendationCommand {

    private final UserRecommendationRepository userRecommendationRepository;

    public List<UserRecommendation> saveAll(List<UserRecommendation> userRecommendations) {

        return userRecommendationRepository.saveAll(userRecommendations);
    }
}
