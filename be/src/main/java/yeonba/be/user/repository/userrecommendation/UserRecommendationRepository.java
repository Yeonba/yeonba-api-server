package yeonba.be.user.repository.userrecommendation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.UserRecommendation;

@Repository
public interface UserRecommendationRepository extends JpaRepository<UserRecommendation, Long> {

}
