package yeonba.be.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class GeneralException extends RuntimeException {

  private final BaseException exception;

  public HttpStatus getHttpStatus() {
    return exception.getHttpStatus();
  }

  public String getExceptionReason() {
    return exception.getReason();
  }
}
