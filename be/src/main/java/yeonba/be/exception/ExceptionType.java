package yeonba.be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

  BAD_REQUEST(
      HttpStatus.BAD_REQUEST,
      "잘못된 요청입니다."),

  INTERNAL_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "서버 에러 관리자에게 문의 바랍니다."),

  // user
  USER_NOT_FOUND(
      HttpStatus.BAD_REQUEST,
      "해당 사용자가 존재하지 않습니다."),

  // report
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

  ExceptionType(HttpStatus httpStatus, String reason) {

    this.httpStatus = httpStatus;
    this.reason = reason;
  }
}
