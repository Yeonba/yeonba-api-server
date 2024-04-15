package yeonba.be.user.repository.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.FavoriteException;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.Favorite;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class FavoriteQuery {

	private final FavoriteRepository favoriteRepository;

	public boolean isFavoriteExist(User user, User favoriteUser) {

		return favoriteRepository.existsByUserAndFavoriteUser(user, favoriteUser);
	}

	public Favorite find(User user, User favoriteUser) {

		return favoriteRepository.findByUserAndFavoriteUser(user, favoriteUser)
			.orElseThrow(() -> new GeneralException(FavoriteException.FAVORITE_NOT_FOUND));
	}
}
