package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public interface BaseException {

  HttpStatus getHttpStatus();

  String getReason();
}
