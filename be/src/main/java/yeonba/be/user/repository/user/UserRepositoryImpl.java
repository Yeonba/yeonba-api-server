package yeonba.be.user.repository.user;

import static yeonba.be.arrow.entity.QArrowTransaction.arrowTransaction;
import static yeonba.be.mypage.entity.QAcquaintance.acquaintance;
import static yeonba.be.user.entity.QAnimal.animal;
import static yeonba.be.user.entity.QArea.area;
import static yeonba.be.user.entity.QFavorite.favorite;
import static yeonba.be.user.entity.QProfilePhoto.profilePhoto;
import static yeonba.be.user.entity.QUser.user;
import static yeonba.be.user.entity.QUserPreference.userPreference;
import static yeonba.be.user.entity.QUserRecommendation.userRecommendation;
import static yeonba.be.user.entity.QUserSearchLog.userSearchLog;
import static yeonba.be.user.entity.QVocalRange.vocalRange;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import yeonba.be.user.dto.response.UserQueryResponse;
import yeonba.be.user.entity.UserPreference;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserQueryResponse> findFavoritesBy(long userId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.constant(true))
            .where(
                isActiveAndNotDeletedUserCondition(),
                findOneFavoriteBy(userId).exists())
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                isActiveAndNotDeletedUserCondition(),
                findOneFavoriteBy(userId).exists());

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findArrowReceiversBy(long senderId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.as(findOneFavoriteBy(senderId).exists(), "favorite"))
            .where(
                isActiveAndNotDeletedUserCondition(),
                findOneArrowSentTransactionBy(senderId).exists())
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                isActiveAndNotDeletedUserCondition(),
                findOneArrowSentTransactionBy(senderId).exists());

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findArrowSendersBy(long receiverId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.as(findOneFavoriteBy(receiverId).exists(), "favorite"))
            .where(
                isActiveAndNotDeletedUserCondition(),
                findOneArrowReceivedTransactionBy(receiverId).exists())
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(
                isActiveAndNotDeletedUserCondition(),
                findOneArrowReceivedTransactionBy(receiverId).exists());

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findRecommendUsers(
        long userId,
        boolean userGender,
        PageRequest pageRequest,
        LocalDate recommendDay) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        // 추천 대상 사용자의 선호조건 조회
        UserPreference preference = queryFactory.selectFrom(userPreference)
            .where(userPreference.user.id.eq(userId))
            .fetchFirst();

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.constant(false))
            .where(recommendUserCondition(userId, userGender, preference, recommendDay))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(recommendUserCondition(userId, userGender, preference, recommendDay));

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    /*
    이성 추천시 배제되는 사용자
    - 자기 자신(조회하는 사용자)
    - 동성
    - 추천(선호) 조건을 만족하지 않는 사용자
    - 화살을 주고 받은 적이 있는 사용자
    - 즐겨찾기한 사용자
    - 삭제, 휴면 상태인 사용자
    - 지인(전화번호로 구분)
    - 추천 일자에 이미 추천된 사용자
    - 추천 일자에 검색된 적 있는 사용자
     */
    private BooleanExpression recommendUserCondition(
        long userId,
        Boolean gender,
        UserPreference preference,
        LocalDate recommendDay) {

        return Expressions.allOf(
            user.id.ne(userId),
            user.gender.ne(gender),
            findOneArrowReceivedTransactionBy(userId).notExists(),
            findOneArrowSentTransactionBy(userId).notExists(),
            findOneFavoriteBy(userId).notExists(),
            isUserSatisfiedPreferenceCondition(preference),
            isActiveAndNotDeletedUserCondition(),
            isNotAcquaintanceCondition(userId),
            isNotUserRecommendedInDateCondition(userId, recommendDay),
            isNotUserSearchedInDateCondition(userId, recommendDay));
    }

    private JPQLQuery<Integer> findOneArrowReceivedTransactionBy(long receiverId) {

        return JPAExpressions.selectOne()
            .from(arrowTransaction)
            .where(
                arrowTransaction.receiver.id.eq(receiverId),
                arrowTransaction.sender.id.eq(user.id));
    }

    private JPQLQuery<Integer> findOneFavoriteBy(long userId) {

        return JPAExpressions.selectOne()
            .from(favorite)
            .where(
                favorite.user.id.eq(userId),
                favorite.favoriteUser.id.eq(user.id));
    }

    private JPQLQuery<Integer> findOneArrowSentTransactionBy(long senderId) {

        return JPAExpressions.selectOne()
            .from(arrowTransaction)
            .where(
                arrowTransaction.sender.id.eq(senderId),
                arrowTransaction.receiver.id.eq(user.id));
    }

    private BooleanExpression isUserSatisfiedPreferenceCondition(UserPreference preference) {

        return Expressions.allOf(
            user.age.between(
                preference.getAgeLowerBound(),
                preference.getAgeUpperBound()),
            user.height.between(
                preference.getHeightLowerBound(),
                preference.getHeightUpperBound()),
            user.mbti.eq(preference.getMbti()),
            user.bodyType.eq(preference.getBodyType()),
            user.vocalRange.id.eq(preference.getVocalRange().getId()),
            user.area.id.eq(preference.getArea().getId()),
            user.animal.id.eq(preference.getAnimal().getId()));
    }

    private BooleanExpression isNotUserRecommendedInDateCondition(
        long userId,
        LocalDate recommendDay) {

        LocalDateTime from = recommendDay.atStartOfDay();
        LocalDateTime to = recommendDay.atTime(LocalTime.MAX);

        return JPAExpressions.selectOne()
            .from(userRecommendation)
            .where(
                userRecommendation.user.id.eq(userId),
                userRecommendation.recommendedUser.id.eq(user.id),
                userRecommendation.createdAt.between(from, to))
            .notExists();
    }

    private BooleanExpression isNotUserSearchedInDateCondition(
        long userId,
        LocalDate searchDate) {

        LocalDateTime from = searchDate.atStartOfDay();
        LocalDateTime to = searchDate.atTime(LocalTime.MAX);

        return JPAExpressions.selectOne()
            .from(userSearchLog)
            .where(
                userSearchLog.user.id.eq(userId),
                userSearchLog.searchedUser.id.eq(user.id),
                userSearchLog.createdAt.between(from, to))
            .notExists();
    }

    /*
    응답 dto에 필요한 필드를 select하는 공통 사용 쿼리, 별도 분리
    경우에 따라 즐겨찾기 등록 여부(favorite)을 상수로 주입하기에
    해당 부분만 파라미터로 받도록 구성
     */
    private JPAQuery<UserQueryResponse> selectUserQueryResponse(
        Expression<Boolean> checkFavoriteExistsNestedQuery) {

        return queryFactory
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
                checkFavoriteExistsNestedQuery))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(isRepresentativeProfilePhotoCondition());
    }

    private BooleanExpression isRepresentativeProfilePhotoCondition() {

        return profilePhoto.id.eq(
            JPAExpressions.select(profilePhoto.id.min())
                .from(profilePhoto)
                .where(profilePhoto.user.id.eq(user.id)));
    }

    private BooleanExpression isActiveAndNotDeletedUserCondition() {

        return user.deleted.isFalse()
            .and(user.inactive.isFalse());
    }

    private BooleanExpression isNotAcquaintanceCondition(long userId) {

        return JPAExpressions.selectOne()
            .from(acquaintance)
            .where(
                acquaintance.userId.eq(userId),
                acquaintance.phoneNumber.eq(user.phoneNumber))
            .notExists();
    }
}