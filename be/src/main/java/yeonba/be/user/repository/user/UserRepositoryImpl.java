package yeonba.be.user.repository.user;

import static yeonba.be.arrow.entity.QArrowTransaction.arrowTransaction;
import static yeonba.be.mypage.entity.QAcquaintance.acquaintance;
import static yeonba.be.user.entity.QAnimal.animal;
import static yeonba.be.user.entity.QArea.area;
import static yeonba.be.user.entity.QBlock.block;
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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import yeonba.be.arrow.enums.ArrowTransactionType;
import yeonba.be.user.dto.request.UserSearchRequest;
import yeonba.be.user.dto.response.UserQueryResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.entity.UserPreference;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserQueryResponse> findFavoritesBy(long userId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = selectUserQueryResponse(Expressions.constant(true))
            .where(findFavoritesCondition(userId))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(findFavoritesCondition(userId));

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    private BooleanExpression findFavoritesCondition(long userId) {

        return Expressions.allOf(
            isActiveAndNotDeletedUserCondition(),
            isNotBlockedUserCondition(userId),
            findOneFavoriteBy(userId).exists());
    }

    @Override
    public Page<UserQueryResponse> findArrowReceiversBy(long senderId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.as(findOneFavoriteBy(senderId).exists(), "favorite"))
            .where(findArrowReceiversCondition(senderId))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(findArrowReceiversCondition(senderId));

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    private BooleanExpression findArrowReceiversCondition(long senderId) {

        return Expressions.allOf(
            isActiveAndNotDeletedUserCondition(),
            isNotBlockedUserCondition(senderId),
            findOneArrowSentTransactionBy(senderId).exists());
    }

    @Override
    public Page<UserQueryResponse> findArrowSendersBy(long receiverId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.as(findOneFavoriteBy(receiverId).exists(), "favorite"))
            .where(findArrowSendersCondition(receiverId))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(findArrowSendersCondition(receiverId));

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    private BooleanExpression findArrowSendersCondition(long receiverId) {

        return Expressions.allOf(
            isActiveAndNotDeletedUserCondition(),
            isNotBlockedUserCondition(receiverId),
            findOneArrowReceivedTransactionBy(receiverId).exists());
    }

    @Override
    public Page<UserQueryResponse> findRecommendUsers(
        User queryingUser,
        PageRequest pageRequest,
        LocalDate recommendDay) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        // 추천 대상 사용자의 선호조건 조회
        UserPreference preference = queryFactory.selectFrom(userPreference)
            .where(userPreference.user.id.eq(queryingUser.getId()))
            .fetchFirst();

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.constant(false))
            .where(recommendUserCondition(queryingUser, preference, recommendDay))
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(recommendUserCondition(queryingUser, preference, recommendDay));

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    /*
    이성 추천시 배제되는 사용자
    - 같은 성별 사용자
    - 추천(선호) 조건을 만족하지 않는 사용자
    - 화살을 주고 받은 적이 있는 사용자
    - 즐겨찾기한 사용자
    - 삭제, 휴면 상태인 사용자
    - 지인(전화번호로 구분)
    - 추천 일자에 이미 추천된 사용자
    - 추천 일자에 검색된 적 있는 사용자
     */
    private BooleanExpression recommendUserCondition(
        User queryingUser,
        UserPreference preference,
        LocalDate recommendDay) {

        long userId = queryingUser.getId();

        return Expressions.allOf(
            notSameGenderCondition(queryingUser.getGenderBoolean()),
            isActiveAndNotDeletedUserCondition(),
            isNotAcquaintanceCondition(userId),
            isNotBlockedUserCondition(userId),
            isUserSatisfiedPreferenceCondition(preference),
            findOneArrowReceivedTransactionBy(userId).notExists(),
            findOneArrowSentTransactionBy(userId).notExists(),
            findOneFavoriteBy(userId).notExists(),
            isNotUserRecommendedOnDayCondition(userId, recommendDay),
            isNotUserSearchedOnDayCondition(userId, recommendDay));
    }

    @Override
    public Page<UserQueryResponse> findUsersBySearchCondition(
        User searchingUser,
        PageRequest pageRequest,
        LocalDate searchDay,
        UserSearchRequest request) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        BooleanExpression searchUserCondition =
            searchUserCondition(searchingUser, request, searchDay);

        List<UserQueryResponse> content = selectUserQueryResponse(
            Expressions.as(findOneFavoriteBy(searchingUser.getId()).exists(), "isFavorite"))
            .from(user)
            .where(searchUserCondition)
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(searchUserCondition);

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    /*
    이성 검색시 제외되는 사용자
    - 같은 성별 사용자
    - 지인
    - 차단한 사용자
    - 휴면, 삭제 상태 사용자
    - 화실을 주고 받은 적 있는 사용자
    - 검색일에 이미 검색된 적 있는 사용자
     */
    // TODO : 채팅 이력 있는 사용자 제외 조건 추가
    private BooleanExpression searchUserCondition(
        User searchingUser, UserSearchRequest request, LocalDate searchDay) {

        long userId = searchingUser.getId();

        return Expressions.allOf(
            notSameGenderCondition(searchingUser.getGenderBoolean()),
            isNotAcquaintanceCondition(userId),
            isNotBlockedUserCondition(userId),
            isActiveAndNotDeletedUserCondition(),
            findOneArrowReceivedTransactionBy(userId).notExists(),
            findOneArrowSentTransactionBy(userId).notExists(),
            isNotUserRecommendedOnDayCondition(userId, searchDay),
            isNotUserSearchedOnDayCondition(userId, searchDay),
            searchCondition(userId, request)
        );
    }

    private BooleanExpression searchCondition(long userId, UserSearchRequest request) {

        if (Objects.isNull(request)) {

            return null;
        }

        String area = request.getArea();
        BooleanExpression userAreaEqualCondition =
            StringUtils.hasText(area) ? user.area.name.eq(area) : null;

        String vocalRange = request.getVocalRange();
        BooleanExpression userVocalRangeEqualCondition =
            StringUtils.hasText(vocalRange) ? user.vocalRange.classification.eq(vocalRange) : null;

        BooleanExpression userAgeRangeCondition =
            userAgeRangeCondition(request.getAgeLowerBound(), request.getAgeUpperBound());

        BooleanExpression userHeightRangeCondition =
            userHeightRangeCondition(request.getHeightLowerBound(), request.getHeightUpperBound());

        BooleanExpression includePreferredAnimalCondition =
            includePreferredAnimalCondition(userId, request.getIncludePreferredAnimal());

        return Expressions.allOf(
            userAreaEqualCondition,
            userVocalRangeEqualCondition,
            userAgeRangeCondition,
            userHeightRangeCondition,
            includePreferredAnimalCondition);
    }

    private BooleanExpression includePreferredAnimalCondition(
        long userId, Boolean includePreferredAnimal) {

        if (Objects.isNull(includePreferredAnimal) || !includePreferredAnimal) {

            return null;
        }

        Long preferredAnimalId = queryFactory.select(userPreference.animal.id)
            .from(userPreference)
            .where(userPreference.user.id.eq(userId))
            .fetchFirst();

        return Objects.nonNull(preferredAnimalId) ? user.animal.id.eq(preferredAnimalId) : null;
    }

    private JPQLQuery<Integer> findOneFavoriteBy(long userId) {

        return JPAExpressions.selectOne()
            .from(favorite)
            .where(favorite.user.id.eq(userId), favorite.favoriteUser.id.eq(user.id));
    }

    private JPQLQuery<Integer> findOneArrowSentTransactionBy(long senderId) {

        return JPAExpressions.selectOne()
            .from(arrowTransaction)
            .where(
                arrowTransaction.type.eq(ArrowTransactionType.USER_TO_USER),
                arrowTransaction.sender.id.eq(senderId),
                arrowTransaction.receiver.id.eq(user.id));
    }

    private JPQLQuery<Integer> findOneArrowReceivedTransactionBy(long receiverId) {

        return JPAExpressions.selectOne()
            .from(arrowTransaction)
            .where(
                arrowTransaction.type.eq(ArrowTransactionType.USER_TO_USER),
                arrowTransaction.receiver.id.eq(receiverId),
                arrowTransaction.sender.id.eq(user.id));
    }

    private BooleanExpression isUserSatisfiedPreferenceCondition(UserPreference preference) {

        return Expressions.allOf(
            user.mbti.eq(preference.getMbti()),
            user.bodyType.eq(preference.getBodyType()),
            user.vocalRange.id.eq(preference.getVocalRange().getId()),
            user.area.id.eq(preference.getArea().getId()),
            user.animal.id.eq(preference.getAnimal().getId()),
            userAgeRangeCondition(preference.getAgeLowerBound(), preference.getAgeUpperBound()),
            userHeightRangeCondition(
                preference.getHeightLowerBound(), preference.getHeightUpperBound())
        );
    }

    private BooleanExpression isNotUserRecommendedOnDayCondition(
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

    private BooleanExpression isNotUserSearchedOnDayCondition(
        long userId,
        LocalDate searchDay) {

        LocalDateTime from = searchDay.atStartOfDay();
        LocalDateTime to = searchDay.atTime(LocalTime.MAX);

        return JPAExpressions.selectOne()
            .from(userSearchLog)
            .where(
                userSearchLog.user.id.eq(userId),
                userSearchLog.searchedUser.id.eq(user.id),
                userSearchLog.createdAt.between(from, to))
            .notExists();
    }

    /*
    응답 dto에 필요한 필드를 select하는 공통 사용 쿼리, 사용하는 로직에 따라
    조회하는 사용자, 조회되는 사용자간 즐겨찾기 존재 여부를 상수로 주입, 혹은 서브 쿼리로 확인
     */
    private JPAQuery<UserQueryResponse> selectUserQueryResponse(
        Expression<Boolean> checkFavoriteExistsNestedQuery) {

        return queryFactory
            .select(
                Projections.constructor(UserQueryResponse.class,
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

    private BooleanExpression notSameGenderCondition(boolean userGender) {

        return user.gender.ne(userGender);
    }

    private BooleanExpression isRepresentativeProfilePhotoCondition() {

        return profilePhoto.id.eq(
            JPAExpressions.select(profilePhoto.id.min())
                .from(profilePhoto)
                .where(profilePhoto.user.id.eq(user.id)));
    }

    private BooleanExpression isActiveAndNotDeletedUserCondition() {

        return user.deleted.isFalse().and(user.inactive.isFalse());
    }

    private BooleanExpression isNotAcquaintanceCondition(long userId) {

        return JPAExpressions.selectOne()
            .from(acquaintance)
            .where(
                acquaintance.userId.eq(userId),
                acquaintance.phoneNumber.eq(user.phoneNumber))
            .notExists();
    }

    private BooleanExpression isNotBlockedUserCondition(long userId) {

        return JPAExpressions.selectOne()
            .from(block)
            .where(
                block.user.id.eq(userId),
                block.blockedUser.id.eq(user.id))
            .notExists();
    }

    private BooleanExpression userAgeRangeCondition(
        Integer ageLowerBound, Integer ageUpperBound) {

        BooleanExpression userAgeGoeCondition =
            Objects.nonNull(ageLowerBound) ? user.age.goe(ageLowerBound) : null;
        BooleanExpression userAgeLoeCondition =
            Objects.nonNull(ageUpperBound) ? user.age.loe(ageUpperBound) : null;

        return Expressions.allOf(userAgeLoeCondition, userAgeGoeCondition);
    }

    private BooleanExpression userHeightRangeCondition(
        Integer heightLowerBound, Integer heightUpperBound) {

        BooleanExpression userHeightGoeCondition =
            Objects.nonNull(heightLowerBound) ? user.height.goe(heightLowerBound) : null;
        BooleanExpression userHeightLoeCondition =
            Objects.nonNull(heightUpperBound) ? user.height.loe(heightUpperBound) : null;

        return Expressions.allOf(userHeightGoeCondition, userHeightLoeCondition);
    }
}
