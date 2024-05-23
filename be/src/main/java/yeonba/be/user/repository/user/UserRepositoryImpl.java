package yeonba.be.user.repository.user;

import static yeonba.be.arrow.entity.QArrowTransaction.arrowTransaction;
import static yeonba.be.arrow.enums.ArrowTransactionType.USER_TO_USER;
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

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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

        List<UserQueryResponse> content = queryFactory.select(
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
                    Expressions.constant(true)))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                profilePhoto.id.eq(
                    JPAExpressions.select(profilePhoto.id.min())
                        .from(profilePhoto)
                        .where(profilePhoto.user.id.eq(user.id))),
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(userId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(favorite)
                    .where(favorite.user.id.eq(userId), favorite.favoriteUser.id.eq(user.id))
                    .exists()
            )
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(userId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(favorite)
                    .where(favorite.user.id.eq(userId), favorite.favoriteUser.id.eq(user.id))
                    .exists()
            );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findArrowReceiversBy(long senderId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = queryFactory.select(
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
                    JPAExpressions.selectOne()
                        .from(favorite)
                        .where(
                            favorite.user.id.eq(senderId),
                            favorite.favoriteUser.id.eq(user.id))
                        .exists()
                        .as("favorite")))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                profilePhoto.id.eq(
                    JPAExpressions.select(profilePhoto.id.min())
                        .from(profilePhoto)
                        .where(profilePhoto.user.id.eq(user.id))),
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(senderId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.type.eq(USER_TO_USER),
                        arrowTransaction.sender.id.eq(senderId),
                        arrowTransaction.receiver.id.eq(user.id)
                    )
                    .exists()
            )
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(senderId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.type.eq(USER_TO_USER),
                        arrowTransaction.sender.id.eq(senderId),
                        arrowTransaction.receiver.id.eq(user.id)
                    )
                    .exists()
            );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findArrowSendersBy(long receiverId, PageRequest pageRequest) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        List<UserQueryResponse> content = queryFactory.select(
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
                    JPAExpressions.selectOne()
                        .from(favorite)
                        .where(
                            favorite.user.id.eq(receiverId),
                            favorite.favoriteUser.id.eq(user.id))
                        .exists()
                        .as("favorite")))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                profilePhoto.id.eq(
                    JPAExpressions.select(profilePhoto.id.min())
                        .from(profilePhoto)
                        .where(profilePhoto.user.id.eq(user.id))),
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(receiverId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.type.eq(USER_TO_USER),
                        arrowTransaction.sender.id.eq(user.id),
                        arrowTransaction.receiver.id.eq(receiverId)
                    )
                    .exists()
            )
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(receiverId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.type.eq(USER_TO_USER),
                        arrowTransaction.sender.id.eq(user.id),
                        arrowTransaction.receiver.id.eq(receiverId)
                    )
                    .exists()
            );

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
    @Override
    public Page<UserQueryResponse> findRecommendUsers(
        User queryingUser,
        PageRequest pageRequest,
        LocalDate recommendDay) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        long userId = queryingUser.getId();

        // 추천 대상 사용자의 선호조건 조회
        UserPreference preference = queryFactory.selectFrom(userPreference)
            .where(userPreference.user.id.eq(userId))
            .fetchFirst();

        LocalDateTime from = recommendDay.atStartOfDay();
        LocalDateTime to = recommendDay.atTime(LocalTime.MAX);

        List<UserQueryResponse> content = queryFactory.select(
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
                    Expressions.constant(false)
                ))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                profilePhoto.id.eq(
                    JPAExpressions.select(profilePhoto.id.min())
                        .from(profilePhoto)
                        .where(profilePhoto.user.id.eq(user.id))),
                user.gender.ne(queryingUser.getGenderBoolean()),
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(userId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(userId),
                        arrowTransaction.receiver.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(user.id),
                        arrowTransaction.receiver.id.eq(userId)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(favorite)
                    .where(
                        favorite.user.id.eq(userId),
                        favorite.favoriteUser.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(acquaintance)
                    .where(
                        acquaintance.userId.eq(userId),
                        acquaintance.name.eq(user.nickname),
                        acquaintance.phoneNumber.eq(user.phoneNumber)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userRecommendation)
                    .where(
                        userRecommendation.user.id.eq(userId),
                        userRecommendation.recommendedUser.id.eq(user.id),
                        userRecommendation.createdAt
                            .between(from, to)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userSearchLog)
                    .where(
                        userSearchLog.user.id.eq(userId),
                        userSearchLog.searchedUser.id.eq(user.id),
                        userSearchLog.createdAt
                            .between(from, to)
                    )
                    .notExists(),
                user.mbti.eq(preference.getMbti()),
                user.bodyType.eq(preference.getBodyType()),
                user.vocalRange.id.eq(preference.getVocalRange().getId()),
                user.area.id.eq(preference.getArea().getId()),
                user.animal.id.eq(preference.getAnimal().getId()),
                Objects.isNull(preference.getAgeLowerBound()) ? null
                    : user.age.goe(preference.getAgeLowerBound()),
                Objects.isNull(preference.getAgeUpperBound()) ? null
                    : user.age.loe(preference.getAgeUpperBound()),
                Objects.isNull(preference.getHeightLowerBound()) ? null
                    : user.height.goe(preference.getHeightLowerBound()),
                Objects.isNull(preference.getHeightUpperBound()) ? null
                    : user.height.loe(preference.getHeightUpperBound())
            )
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(
                user.gender.ne(queryingUser.getGenderBoolean()),
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(acquaintance)
                    .where(
                        acquaintance.userId.eq(userId),
                        acquaintance.name.eq(user.nickname),
                        acquaintance.phoneNumber.eq(user.phoneNumber)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(userId),
                        block.blockedUser.id.eq(user.id))
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(userId),
                        arrowTransaction.receiver.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(user.id),
                        arrowTransaction.receiver.id.eq(userId)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(favorite)
                    .where(
                        favorite.user.id.eq(userId),
                        favorite.favoriteUser.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userRecommendation)
                    .where(
                        userRecommendation.user.id.eq(userId),
                        userRecommendation.recommendedUser.id.eq(user.id),
                        userRecommendation.createdAt.between(from, to)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userSearchLog)
                    .where(
                        userSearchLog.user.id.eq(userId),
                        userSearchLog.searchedUser.id.eq(user.id),
                        userSearchLog.createdAt.between(from, to)
                    )
                    .notExists(),
                user.mbti.eq(preference.getMbti()),
                user.bodyType.eq(preference.getBodyType()),
                user.vocalRange.id.eq(preference.getVocalRange().getId()),
                user.area.id.eq(preference.getArea().getId()),
                user.animal.id.eq(preference.getAnimal().getId()),
                Objects.isNull(preference.getAgeLowerBound()) ? null
                    : user.age.goe(preference.getAgeLowerBound()),
                Objects.isNull(preference.getAgeUpperBound()) ? null
                    : user.age.loe(preference.getAgeUpperBound()),
                Objects.isNull(preference.getHeightLowerBound()) ? null
                    : user.height.goe(preference.getHeightLowerBound()),
                Objects.isNull(preference.getHeightUpperBound()) ? null
                    : user.height.loe(preference.getHeightUpperBound())
            );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }

    @Override
    public Page<UserQueryResponse> findUsersBySearchCondition(
        User searchingUser,
        PageRequest pageRequest,
        LocalDate searchDay,
        UserSearchRequest request) {

        int limit = pageRequest.getPageSize();
        int offset = pageRequest.getPageNumber() * limit;

        long userId = searchingUser.getId();
        LocalDateTime from = searchDay.atStartOfDay();
        LocalDateTime to = searchDay.atTime(LocalTime.MAX);

        BooleanExpression userAreaEq =
            Objects.nonNull(request) && Objects.nonNull(request.getArea()) ?
                user.area.name.eq(request.getArea()) : null;
        BooleanExpression userVocalRangeEq =
            Objects.nonNull(request) && Objects.nonNull(request.getVocalRange()) ?
                user.vocalRange.classification.eq(request.getVocalRange()) : null;
        BooleanExpression userAgeGoe =
            Objects.nonNull(request) && Objects.nonNull(request.getAgeLowerBound()) ?
                user.age.goe(request.getAgeLowerBound()) : null;
        BooleanExpression userAgeLoe =
            Objects.nonNull(request) && Objects.nonNull(request.getAgeUpperBound()) ?
                user.age.loe(request.getAgeUpperBound()) : null;
        BooleanExpression userHeightGoe =
            Objects.nonNull(request) && Objects.nonNull(request.getHeightLowerBound()) ?
                user.height.goe(request.getHeightLowerBound()) : null;
        BooleanExpression userHeightLoe =
            Objects.nonNull(request) && Objects.nonNull(request.getHeightUpperBound()) ?
                user.height.loe(request.getHeightUpperBound()) : null;

        BooleanExpression userAnimalEq = null;
        if (Objects.nonNull(request)) {
            Boolean includePreferredAnimal = request.getIncludePreferredAnimal();

            if (Objects.nonNull(includePreferredAnimal) && includePreferredAnimal) {
                Long preferredAnimalId = queryFactory.select(userPreference.animal.id)
                    .from(userPreference)
                    .where(userPreference.user.id.eq(userId))
                    .fetchFirst();

                userAnimalEq = Objects.nonNull(preferredAnimalId) ?
                    user.animal.id.eq(preferredAnimalId) : null;
            }
        }

        BooleanExpression searchCondition = Expressions.allOf(
            userAreaEq, userVocalRangeEq,
            userAgeGoe, userAgeLoe,
            userHeightGoe, userHeightLoe,
            userAnimalEq
        );

        List<UserQueryResponse> content = queryFactory.select(
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
                    JPAExpressions.selectOne()
                        .from(favorite)
                        .where(
                            favorite.user.id.eq(userId),
                            favorite.favoriteUser.id.eq(user.id)
                        )
                        .exists()
                        .as("favorite")
                ))
            .from(user)
            .innerJoin(user.animal, animal)
            .innerJoin(user.area, area)
            .innerJoin(user.vocalRange, vocalRange)
            .innerJoin(user.profilePhotos, profilePhoto)
            .where(
                profilePhoto.id.eq(
                    JPAExpressions.select(profilePhoto.id.min())
                        .from(profilePhoto)
                        .where(profilePhoto.user.id.eq(user.id))),
                user.gender.ne(searchingUser.getGenderBoolean()),
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(acquaintance)
                    .where(
                        acquaintance.userId.eq(userId),
                        acquaintance.name.eq(user.nickname),
                        acquaintance.phoneNumber.eq(user.phoneNumber)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(userId),
                        block.blockedUser.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(userId),
                        arrowTransaction.receiver.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(user.id),
                        arrowTransaction.receiver.id.eq(userId)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userRecommendation)
                    .where(
                        userRecommendation.user.id.eq(userId),
                        userRecommendation.recommendedUser.id.eq(user.id),
                        userRecommendation.createdAt.between(from, to)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userSearchLog)
                    .where(
                        userSearchLog.user.id.eq(userId),
                        userSearchLog.searchedUser.id.eq(user.id),
                        userSearchLog.createdAt.between(from, to)
                    )
                    .notExists(),
                searchCondition
            )
            .limit(limit)
            .offset(offset)
            .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(user.count())
            .from(user)
            .where(
                user.gender.ne(searchingUser.getGenderBoolean()),
                user.inactive.isFalse(),
                user.deleted.isFalse(),
                JPAExpressions.selectOne()
                    .from(acquaintance)
                    .where(
                        acquaintance.userId.eq(userId),
                        acquaintance.name.eq(user.nickname),
                        acquaintance.phoneNumber.eq(user.phoneNumber)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(block)
                    .where(
                        block.user.id.eq(userId),
                        block.blockedUser.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(userId),
                        arrowTransaction.receiver.id.eq(user.id)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(arrowTransaction)
                    .where(
                        arrowTransaction.sender.id.eq(user.id),
                        arrowTransaction.receiver.id.eq(userId)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userRecommendation)
                    .where(
                        userRecommendation.user.id.eq(userId),
                        userRecommendation.recommendedUser.id.eq(user.id),
                        userRecommendation.createdAt.between(from, to)
                    )
                    .notExists(),
                JPAExpressions.selectOne()
                    .from(userSearchLog)
                    .where(
                        userSearchLog.user.id.eq(userId),
                        userSearchLog.searchedUser.id.eq(user.id),
                        userSearchLog.createdAt.between(from, to)
                    )
                    .notExists(),
                searchCondition
            );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }
}
