package yeonba.be.arrow.repository;

import static yeonba.be.arrow.entity.QArrowTransaction.arrowTransaction;
import static yeonba.be.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArrowTransactionRepositoryImpl implements ArrowTransactionRepositoryCustom {

    private final int MAX_ARROW_COUNT_OF_AD = 3;
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean canChargeByAdvertisement(long userId, LocalDateTime today) {

        long countOfTodayAdView = Optional.ofNullable(
                queryFactory
                    .select(arrowTransaction.count())
                    .from(arrowTransaction)
                    .innerJoin(arrowTransaction.receiver, user)
                    .where(
                        arrowTransaction.sender.isNull(),
                        arrowTransaction.receiver.id.eq(userId),
                        arrowTransaction.createdAt.after(today)
                    )
                    .fetchOne())
            .orElse(0L);

        return countOfTodayAdView < MAX_ARROW_COUNT_OF_AD;
    }
}
