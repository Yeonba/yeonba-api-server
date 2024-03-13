package yeonba.be.user.repository.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class FavoriteQuery {

  private final FavoriteRepository favoriteRepository;

  public boolean isFavoriteExist(User user, User favoriteUser) {

    return favoriteRepository.existsByUserAndFavoriteUser(user, favoriteUser);
  }
}
