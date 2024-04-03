package yeonba.be.user.repository.userrecommendation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class UserRecommendationQuery {

    private final UserRecommendationRepository userRecommendationRepository;

    public boolean isRecommendationInSameDateExistBy(User user, LocalDate recommendDate) {

        LocalDateTime from = recommendDate.atStartOfDay();
        LocalDateTime to = recommendDate.atTime(LocalTime.MAX);

        return userRecommendationRepository.existsByUserAndCreatedAtBetween(user, from, to);
    }
}