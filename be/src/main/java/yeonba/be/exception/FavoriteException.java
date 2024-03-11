package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum FavoriteException implements BaseException {

  ALREADY_FAVORITE_USER(
      HttpStatus.BAD_REQUEST,
      "이미 즐겨찾기한 사용자입니다."),

  FAVORITE_NOT_FOUND(
      HttpStatus.BAD_REQUEST,
      "해당 즐겨찾기 내역이 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String reason;

  FavoriteException(HttpStatus httpStatus, String reason) {
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
