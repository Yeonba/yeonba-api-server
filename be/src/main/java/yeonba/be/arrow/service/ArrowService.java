package yeonba.be.arrow.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.dto.response.UserArrowsResponse;
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

    private final int ADVERTISEMENT_ARROW_COUNT = 5;
    private final UserQuery userQuery;
    private final ArrowCommand arrowCommand;
    private final ArrowQuery arrowQuery;

    @Transactional
    public void dailyCheck(long userId, LocalDate dailyCheckDay) {

        User dailyCheckUser = userQuery.findById(userId);

        // 휴면 상태 사용자는 출석 체크 불가
        if (dailyCheckUser.isInactive()) {
            throw new GeneralException(UserException.INACTIVE_USER);
        }

        // 처음 가입한 사용자는 최종 접속 일시가 null, 이 경우 출석 체크를 그냥 진행함
        if (!Objects.isNull(dailyCheckUser.getLastAccessedAt())) {
            dailyCheckUser.validateDailyCheck(dailyCheckDay);
        }

        int dailyCheckArrows = 10;
        ArrowTransaction arrowTransaction = new ArrowTransaction(dailyCheckUser, dailyCheckArrows);
        arrowCommand.save(arrowTransaction);

        dailyCheckUser.plusArrow(dailyCheckArrows);
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
