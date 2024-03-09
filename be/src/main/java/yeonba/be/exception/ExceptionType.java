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

  USER_NOT_FOUND(
      HttpStatus.BAD_REQUEST,
      "해당 사용자가 존재하지 않습니다."),

  // block
  CAN_NOT_BLOCK_SELF(
      HttpStatus.BAD_REQUEST,
      "자기 자신을 차단할 수 없습니다."),

  ALREADY_BLOCKED_USER(
      HttpStatus.BAD_REQUEST,
      "이미 차단한 사용자입니다.");

  private final HttpStatus httpStatus;
  private final String reason;

  ExceptionType(HttpStatus httpStatus, String reason) {
    this.httpStatus = httpStatus;
    this.reason = reason;
  }
}
