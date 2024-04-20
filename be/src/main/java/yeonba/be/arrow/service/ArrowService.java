package yeonba.be.arrow.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.dto.UserArrowsResponse;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.arrow.repository.ArrowCommand;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.exception.ArrowException;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class ArrowService {

    private final int DAILY_CHECK_ARROW_COUNT = 10;
    private final int ADVERTISEMENT_ARROW_COUNT = 5;
    private final UserQuery userQuery;
    private final ArrowCommand arrowCommand;
    private final ArrowQuery arrowQuery;

    /*
      출석 체크는 다음 과정을 거쳐 이뤄진다.
      1. 사용자 최종 접속 일시를 통해 이미 출석 체크하였는지 확인
      2. 화살 송수신 내역 저장
      3. 사용자 최종 접속 일시 갱신
      4. 사용자 화살 개수 증가
     */
    @Transactional
    public void dailyCheck(long userId) {

        User dailyCheckUser = userQuery.findById(userId);

        LocalDateTime dailyCheckedAt = LocalDateTime.now();
        dailyCheckUser.validateDailyCheck(dailyCheckedAt.toLocalDate());

        ArrowTransaction arrowTransaction = new ArrowTransaction(
            dailyCheckUser,
            DAILY_CHECK_ARROW_COUNT);
        arrowCommand.save(arrowTransaction);

        dailyCheckUser.updateLastAccessedAt(dailyCheckedAt);
        dailyCheckUser.plusArrow(DAILY_CHECK_ARROW_COUNT);
    }

    @Transactional(readOnly = true)
    public UserArrowsResponse getUserArrows(long userId) {

        User user = userQuery.findById(userId);

        return new UserArrowsResponse(user.getArrow());
    }

    @Transactional
    public void sendArrow(long senderId, long receiverId) {

        User sender = userQuery.findById(senderId);
        User receiver = userQuery.findById(receiverId);

        // 휴면 상태에선 화살을 보낼 수 없음
        if (sender.isInactive()) {
            throw new GeneralException(UserException.INACTIVE_USER);
        }

        // 휴면 상태인 사용자에게 화살을 보낼 수 없음
        if (receiver.isInactive()) {
            throw new GeneralException(ArrowException.CAN_NOT_SEND_ARROW_TO_INACTIVE_USER);
        }

        // 자기 자신에게 화살을 보낼 수 없음
        sender.validateNotSameUser(receiver);

        // 같은 성별 사용자에게 화살을 보낼 수 없음
        sender.validateSameGender(receiver);

        // 이미 화살을 보낸 사용자에게 화살을 보낼 수 없음
        if (arrowQuery.isArrowTransactionExist(sender, receiver)) {
            throw new GeneralException(ArrowException.ALREADY_SENT_ARROW_USER);
        }

        // 화살은 1개만 보낼 수 있음
        int sendArrow = 1;
        ArrowTransaction arrowTransaction = new ArrowTransaction(sender, receiver, sendArrow);
        arrowCommand.save(arrowTransaction);

        sender.minusArrow(sendArrow);
        receiver.plusArrow(sendArrow);
    }

    @Transactional
    public void chargeArrows(long userId) {

        User user = userQuery.findById(userId);

        LocalDateTime today = LocalDate.now().atStartOfDay();
        arrowQuery.validateAdvertisementArrowCount(userId, today);

        ArrowTransaction arrowTransaction = new ArrowTransaction(
            user,
            ADVERTISEMENT_ARROW_COUNT);

        arrowCommand.save(arrowTransaction);
        user.plusArrow(ADVERTISEMENT_ARROW_COUNT);
    }

}
