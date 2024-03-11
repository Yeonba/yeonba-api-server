package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum ArrowException implements BaseException {

  ALREADY_CHECKED_USER(
      HttpStatus.BAD_REQUEST,
      "이미 출석 체크한 사용자입니다.");

  private final HttpStatus httpStatus;
  private final String reason;

  ArrowException(HttpStatus httpStatus, String reason) {
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
