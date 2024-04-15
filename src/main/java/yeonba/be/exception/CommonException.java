package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum CommonException implements BaseException {

  BAD_REQUEST(
      HttpStatus.BAD_REQUEST,
      "잘못된 요청입니다."),

  INTERNAL_SERVER_ERROR(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "서버 에러 관리자에게 문의 바랍니다.");

  private final HttpStatus httpStatus;
  private final String reason;

  CommonException(HttpStatus httpStatus, String reason) {
    this.httpStatus = httpStatus;
    this.reason = reason;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @Override
  public String getReason() {
    return reason;
  }
}
