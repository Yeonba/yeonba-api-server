package yeonba.be.arrow.service;

import static yeonba.be.arrow.enums.ArrowTransactionType.DAILY_CHECK;
import static yeonba.be.arrow.enums.ArrowTransactionType.REWARDS_FOR_WATCHING_ADVERTISEMENTS;
import static yeonba.be.arrow.enums.ArrowTransactionType.USER_TO_USER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.dto.response.UserArrowsResponse;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.arrow.repository.ArrowCommand;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.exception.ArrowException;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.notification.enums.NotificationType;
import yeonba.be.notification.event.NotificationSendEvent;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.user.UserQuery;

@Service
@RequiredArgsConstructor
public class ArrowService {

    private final UserQuery userQuery;
    private final ArrowCommand arrowCommand;
    private final ArrowQuery arrowQuery;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public boolean dailyCheck(long userId, LocalDate dailyCheckDay) {

        User dailyCheckUser = userQuery.findById(userId);

        // 휴면 상태 사용자는 출석 체크 불가
        if (dailyCheckUser.isInactive()) {
            throw new GeneralException(UserException.INACTIVE_USER);
        }

        boolean canDailyCheck = dailyCheckUser.canDailyCheckAt(dailyCheckDay);

        // 출석 체크 화살 내역 저장, 사용자 화살 증가
        if (canDailyCheck) {
            int dailyCheckArrows = 10;
            ArrowTransaction arrowTransaction = new ArrowTransaction(
                DAILY_CHECK,
                dailyCheckUser,
                dailyCheckArrows);
            arrowCommand.save(arrowTransaction);

            dailyCheckUser.plusArrow(dailyCheckArrows);
        }

        // 사용자 최종 접속 일시 갱신
        dailyCheckUser.updateLastAccessedAt(LocalDateTime.now());

        return canDailyCheck;
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
        ArrowTransaction arrowTransaction =
            new ArrowTransaction(USER_TO_USER, sender, receiver, sendArrow);
        arrowCommand.save(arrowTransaction);

        sender.minusArrow(sendArrow);
        receiver.plusArrow(sendArrow);

        LocalDateTime createdAt = arrowTransaction.getCreatedAt();
        NotificationSendEvent notificationSendEvent =
            new NotificationSendEvent(NotificationType.ARROW_RECEIVED, sender, receiver, createdAt);
        eventPublisher.publishEvent(notificationSendEvent);
    }

    @Transactional
    public void chargeArrows(long userId, LocalDate chargeDay) {

        User arrowChargeUser = userQuery.findById(userId);

        LocalDateTime chargeDayStartTime = chargeDay.atStartOfDay();
        arrowQuery.validateAdvertisementArrowCount(userId, chargeDayStartTime);

        int chargeArrows = 5;
        ArrowTransaction arrowTransaction = new ArrowTransaction(
            REWARDS_FOR_WATCHING_ADVERTISEMENTS,
            arrowChargeUser,
            chargeArrows);

        arrowCommand.save(arrowTransaction);
        arrowChargeUser.plusArrow(chargeArrows);
    }
}
