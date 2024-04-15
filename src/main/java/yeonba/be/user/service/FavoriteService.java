package yeonba.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.exception.FavoriteException;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.Favorite;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;
import yeonba.be.user.repository.favorite.FavoriteCommand;
import yeonba.be.user.repository.favorite.FavoriteQuery;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteCommand favoriteCommand;
	private final FavoriteQuery favoriteQuery;
	private final UserQuery userQuery;

	/*
	  즐겨찾기 등록 비즈니스 로직은 다음 예외들을 검증
	  1. 자기 자신을 즐겨찾기하는 경우
	  2. 이미 즐겨찾기 사용자를 재등록하려는 경우
	 */
	@Transactional
	public void addFavorite(long userId, long favoriteUserId) {

		User user = userQuery.findById(userId);
		User favoriteUser = userQuery.findById(favoriteUserId);

		user.validateNotSameUser(favoriteUser);

		if (favoriteQuery.isFavoriteExist(user, favoriteUser)) {
			throw new GeneralException(FavoriteException.ALREADY_FAVORITE_USER);
		}

		Favorite favorite = new Favorite(user, favoriteUser);
		favoriteCommand.save(favorite);
	}

	@Transactional
	public void deleteFavorite(long userId, long favoriteUserId) {

		User user = userQuery.findById(userId);
		User favoriteUser = userQuery.findById(favoriteUserId);

		Favorite favorite = favoriteQuery.find(user, favoriteUser);
		favoriteCommand.delete(favorite);
	}
}
