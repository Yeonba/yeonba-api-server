package yeonba.be.user.repository.favorite;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.Favorite;
import yeonba.be.user.entity.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  boolean existsByUserAndFavoriteUser(User user, User favoriteUser);

  Optional<Favorite> findByUserAndFavoriteUser(User user, User favoriteUser);
}
