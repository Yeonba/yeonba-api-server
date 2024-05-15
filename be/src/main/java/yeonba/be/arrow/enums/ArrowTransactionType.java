package yeonba.be.arrow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArrowTransactionType {

    DAILY_CHECK("출석 체크"),

    REWARDS_FOR_WATCHING_ADVERTISEMENTS("광고 시청 통한 화살 획득"),

    USER_TO_USER("사용자간 송수신");

    private final String description;
}
