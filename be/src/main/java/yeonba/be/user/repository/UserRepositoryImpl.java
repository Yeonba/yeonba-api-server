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
추천 이성을 조회하는 경우에만 부가적으로 지인을 배제한다.
지인은 애초에 즐겨찾기 등록, 화살 보내기가 불가능하므로 연관 조회 로직에서 따로 제외하지 않는다.

카운트 쿼리에서는 데이터를 가져오기 위한 불필요한 조인을 수행하지 않는다.
 */

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
                isActiveAndNotDeletedUserCondition(),
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
                isArrowReceiverExistCondition(senderId))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                isActiveAndNotDeletedUserCondition(),
                isArrowReceiverExistCondition(senderId));

        return PageableExecutionUtils.getPage(
            content,
            pageRequest,
            countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findAllArrowSenders(long receiverId, PageRequest pageRequest) {

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
                    isFavoriteExistCondition(receiverId), "isFavorite")))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                isRepresentativeProfilePhotoCondition(),
                isActiveAndNotDeletedUserCondition(),
                isArrowSenderExistCondition(receiverId))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(
                isActiveAndNotDeletedUserCondition(),
                isArrowSenderExistCondition(receiverId));

        return PageableExecutionUtils.getPage(
            content,
            pageRequest,
            countQuery::fetchOne);
    }

    private BooleanExpression isActiveAndNotDeletedUserCondition() {

        return user.deletedAt.isNull()
            .and(user.inactive.isFalse());
    }

    private BooleanExpression isArrowReceiverExistCondition(long senderId) {

        return JPAExpressions.selectOne()
            .from(arrowTransaction)
            .where(
                arrowTransaction.sender.id.eq(senderId),
                arrowTransaction.receiver.id.eq(user.id))
            .exists();
    }

    private BooleanExpression isArrowSenderExistCondition(long receiverId) {

        return JPAExpressions.selectOne()
            .from(arrowTransaction)
            .where(
                arrowTransaction.receiver.id.eq(receiverId),
                arrowTransaction.sender.id.eq(user.id))
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