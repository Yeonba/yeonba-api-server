package yeonba.be.arrow.repository;

import java.time.LocalDateTime;

public interface ArrowTransactionRepositoryCustom {

    boolean canChargeByAdvertisement(long userId, LocalDateTime today);

}
