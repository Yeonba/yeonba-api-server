package yeonba.be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportException implements BaseException {

    REPORT_REASON_NOT_EXIST(
        HttpStatus.BAD_REQUEST,
        "신고 분류가 기타일 때는 사유가 존재해야 합니다."),

    REPORT_REASON_LENGTH_NOT_VALID(
        HttpStatus.BAD_REQUEST,
        "신고 사유는 1024자를 넘을 수 없습니다."),

    CAN_NOT_REPORT_SELF(
        HttpStatus.BAD_REQUEST,
        "자기 자신을 신고할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String reason;
}
