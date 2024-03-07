package yeonba.be.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

  BAD_REQUEST(
      HttpStatus.BAD_REQUEST,
      "COMMON400",
      "잘못된 요청입니다."),

  INTERNAL_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "COMMON500",
      "서버 에러 관리자에게 문의 바랍니다."),

  // 서비스 고유 예외 코드 형식 : {관련 엔티티 이름(대문자)}{HTTP 응답 코드}{고유 번호}
  USER_NOT_FOUND(
      HttpStatus.BAD_REQUEST,
      "USER4001",
      "해당 사용자가 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String reason;

  ExceptionType(HttpStatus httpStatus, String code, String reason) {
    this.httpStatus = httpStatus;
    this.code = code;
    this.reason = reason;
  }
}
