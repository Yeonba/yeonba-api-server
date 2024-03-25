package yeonba.be.arrow.repository;

import static yeonba.be.arrow.entity.QArrowTransaction.arrowTransaction;
import static yeonba.be.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArrowTransactionRepositoryImpl implements ArrowTransactionRepositoryCustom {

    private final int MAX_ARROW_COUNT_OF_AD = 3;
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean canChargeByAdvertisement(long userId, LocalDateTime today) {

        long countOfTodayAdView = queryFactory
            .select(arrowTransaction)
            .from(arrowTransaction)
            .innerJoin(arrowTransaction.receivedUser, user)
            .where(
                arrowTransaction.sentUser.isNull(),
                arrowTransaction.receivedUser.id.eq(userId),
                arrowTransaction.createdAt.after(today)
            )
            .fetchCount();

        return countOfTodayAdView < MAX_ARROW_COUNT_OF_AD;
    }
}
