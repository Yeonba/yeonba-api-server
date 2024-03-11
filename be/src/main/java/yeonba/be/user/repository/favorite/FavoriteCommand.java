package yeonba.be.user.repository.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.Favorite;

@Component
@RequiredArgsConstructor
public class FavoriteCommand {

  private final FavoriteRepository favoriteRepository;

  public Favorite save(Favorite favorite) {

    return favoriteRepository.save(favorite);
  }

  public void delete(Favorite favorite) {
    favoriteRepository.delete(favorite);
  }

}
