package yeonba.be.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class GeneralException extends RuntimeException {

  private final ExceptionType exceptionType;

  public HttpStatus getHttpStatus() {
    return exceptionType.getHttpStatus();
  }

  public String getExceptionReason() {
    return exceptionType.getReason();
  }
}
