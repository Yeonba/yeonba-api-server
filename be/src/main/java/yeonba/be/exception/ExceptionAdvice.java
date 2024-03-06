package yeonba.be.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import yeonba.be.util.CustomResponse;

@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = GeneralException.class)
  public ResponseEntity<Object> handleGeneralException(
      GeneralException exception,
      HttpServletRequest request
  ) {

    return handlerExceptionInternal(
        exception,
        request
    );
  }

  private ResponseEntity<Object> handlerExceptionInternal(
      GeneralException exception,
      HttpServletRequest request
  ) {

    CustomResponse<Object> body = new CustomResponse<>(exception.getExceptionReason());
    WebRequest webRequest = new ServletWebRequest(request);
    return super.handleExceptionInternal(
        exception,
        body,
        HttpHeaders.EMPTY,
        exception.getHttpStatus(),
        webRequest
    );
  }
}
