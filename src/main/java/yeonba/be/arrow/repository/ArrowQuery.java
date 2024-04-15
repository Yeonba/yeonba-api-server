package yeonba.be.arrow.repository;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.ArrowException;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class ArrowQuery {

    private final ArrowTransactionRepository arrowTransactionRepository;

    public boolean isArrowTransactionExist(User sentUser, User receivedUser) {

        return arrowTransactionRepository
            .existsBySentUserAndReceivedUser(sentUser, receivedUser);
    }

    public void validateAdvertisementArrowCount(long userId, LocalDateTime today) {

        if (!arrowTransactionRepository.canChargeByAdvertisement(userId, today)) {
            throw new GeneralException(ArrowException.EXCEEDED_DAILY_AD_VIEWS);
        }
    }
}
