package yeonba.be.user.repository;

import static yeonba.be.user.entity.QAnimal.animal;
import static yeonba.be.user.entity.QArea.area;
import static yeonba.be.user.entity.QFavorite.favorite;
import static yeonba.be.user.entity.QProfilePhoto.profilePhoto;
import static yeonba.be.user.entity.QUser.user;
import static yeonba.be.user.entity.QVocalRange.vocalRange;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import yeonba.be.user.dto.response.UserQueryResponse;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserQueryResponse> findAllFavorites(long userId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = favoriteQuery(userId)
            .select(Projections.constructor(UserQueryResponse.class,
                user.id,
                profilePhoto.photoUrl,
                user.nickname,
                user.age,
                user.arrow,
                animal.name,
                user.photoSyncRate,
                area.name,
                user.height,
                vocalRange.classification,
                Expressions.constant(true)))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = favoriteQuery(userId)
            .select(user.count());

        return PageableExecutionUtils.getPage(
            content,
            pageRequest,
            countQuery::fetchOne);
    }

    /*
    즐겨찾기한 사용자 조회시 아래 조건도 부합해야 한다.
    - 삭제되지 않은 사용자(deleteAt이 null)
    - 휴면 상태가 아닌 사용자(inactive가 false)
     */
    private JPAQuery<?> favoriteQuery(long userId) {

        return queryFactory.from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                profilePhoto.id.eq(
                    JPAExpressions.select(profilePhoto.id.min())
                        .from(profilePhoto)
                        .where(profilePhoto.user.id.eq(user.id))),
                user.deletedAt.isNull(),
                user.inactive.isFalse(),
                JPAExpressions.selectOne()
                    .from(favorite)
                    .where(
                        favorite.user.id.eq(userId),
                        favorite.favoriteUser.id.eq(user.id))
                    .exists());
    }
}