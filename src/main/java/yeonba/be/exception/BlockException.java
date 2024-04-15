package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum BlockException implements BaseException {

  ALREADY_BLOCKED_USER(
      HttpStatus.BAD_REQUEST,
      "이미 차단한 사용자입니다."),

  NOT_BLOCKED_USER(
      HttpStatus.BAD_REQUEST,
      "차단한 사용자가 아닙니다.");

  private final HttpStatus httpStatus;
  private final String reason;

  BlockException(HttpStatus httpStatus, String reason) {
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
