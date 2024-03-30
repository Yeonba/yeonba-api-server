package yeonba.be.user.repository;

import static yeonba.be.arrow.entity.QArrowTransaction.arrowTransaction;
import static yeonba.be.mypage.entity.QAcquaintance.acquaintance;
import static yeonba.be.user.entity.QAnimal.animal;
import static yeonba.be.user.entity.QArea.area;
import static yeonba.be.user.entity.QFavorite.favorite;
import static yeonba.be.user.entity.QProfilePhoto.profilePhoto;
import static yeonba.be.user.entity.QUser.user;
import static yeonba.be.user.entity.QVocalRange.vocalRange;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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

/*
사용자 조회 로직에선 기본적으로 다음 사용자를 배제한다.
- 휴면 상태인 사용자
- 삭제된 사용자
- 지인

카운트 쿼리에서는 데이터를 가져오기 위한 불필요한 조인을 수행하지 않는다.
 */

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 지인은 애초에 즐겨찾기 등록이 불가하므로, 조회 쿼리에서 지인을 배제하는 과정을 따로 거치지 않는다.
    @Override
    public Page<UserQueryResponse> findAllFavorites(long userId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = queryFactory
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
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                isRepresentativeProfilePhotoCondition(),
                user.deletedAt.isNull(),
                user.inactive.isFalse(),
                isFavoriteExistCondition(userId))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                isActiveAndNotDeletedUserCondition(),
                isFavoriteExistCondition(userId));

        return PageableExecutionUtils.getPage(
            content,
            pageRequest,
            countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findAllArrowReceivers(long senderId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = queryFactory
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
                ExpressionUtils.as(
                    isFavoriteExistCondition(senderId), "isFavorite")))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                isRepresentativeProfilePhotoCondition(),
                isActiveAndNotDeletedUserCondition(),
                isNotAcquaintanceCondition(senderId),
                isArrowTransactionExistCondition(senderId))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                isActiveAndNotDeletedUserCondition(),
                isArrowTransactionExistCondition(senderId),
                isArrowTransactionExistCondition(senderId));

        return PageableExecutionUtils.getPage(
            content,
            pageRequest,
            countQuery::fetchOne);
    }

    private BooleanExpression isActiveAndNotDeletedUserCondition() {

        return user.deletedAt.isNull()
            .and(user.inactive.isFalse());
    }

    private BooleanExpression isArrowTransactionExistCondition(long senderId) {

        return JPAExpressions.selectOne()
            .from(arrowTransaction)
            .where(
                arrowTransaction.sender.id.eq(senderId),
                arrowTransaction.receiver.id.eq(user.id))
            .exists();
    }

    private BooleanExpression isFavoriteExistCondition(long userId) {

        return JPAExpressions.selectOne()
            .from(favorite)
            .where(
                favorite.user.id.eq(userId),
                favorite.favoriteUser.id.eq(user.id))
            .exists();
    }

    private BooleanExpression isNotAcquaintanceCondition(long userId) {

        return JPAExpressions.selectOne()
            .from(acquaintance)
            .where(
                acquaintance.user.id.eq(userId),
                acquaintance.phoneNumber.eq(user.phoneNumber))
            .notExists();
    }

    private BooleanExpression isRepresentativeProfilePhotoCondition() {

        return profilePhoto.id.eq(
            JPAExpressions.select(profilePhoto.id.min())
                .from(profilePhoto)
                .where(profilePhoto.user.id.eq(user.id)));
    }
}