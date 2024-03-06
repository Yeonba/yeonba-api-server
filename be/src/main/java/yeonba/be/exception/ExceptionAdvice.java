package yeonba.be.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    return handleExceptionInternal(
        exception,
        request
    );
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException exception,
      WebRequest request
  ) {

    String exceptionMessage = getConstraintViolationMessage(exception);
    return handleExceptionInternalConstraint(
        exception,
        exceptionMessage,
        request
    );
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {

    Map<String, String> exceptionArgs = getMethodArgumentExceptionArgs(exception);
    return handleExceptionInternalArgs(
        exception,
        exceptionArgs,
        request
    );
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleAllException(
      Exception exception,
      WebRequest request
  ) {

    return handleExceptionInternal(
        exception,
        request,
        exception.getMessage()
    );
  }

  private ResponseEntity<Object> handleExceptionInternal(
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

  private Map<String, String> getMethodArgumentExceptionArgs(
      MethodArgumentNotValidException exception) {

    Map<String, String> exceptionArgs = new LinkedHashMap<>();
    exception.getBindingResult().getFieldErrors()
        .forEach(fieldError -> {
          String fieldName = fieldError.getField();
          String errorMessage = Optional
              .ofNullable(fieldError.getDefaultMessage())
              .orElse("");
          exceptionArgs.merge(
              fieldName,
              errorMessage,
              (existingErrorMessage, newErrorMessage) ->
                  existingErrorMessage
                      .concat(", ")
                      .concat(newErrorMessage));
        });

    return exceptionArgs;
  }

  private ResponseEntity<Object> handleExceptionInternalArgs(
      MethodArgumentNotValidException exception,
      Map<String, String> exceptionArgs,
      WebRequest request
  ) {

    CustomResponse<Map<String, String>> body = CustomResponse.onFailure(
        ExceptionType.BAD_REQUEST.getReason(),
        exceptionArgs
    );
    return super.handleExceptionInternal(
        exception,
        body,
        HttpHeaders.EMPTY,
        ExceptionType.BAD_REQUEST.getHttpStatus(),
        request
    );
  }

  private String getConstraintViolationMessage(ConstraintViolationException exception) {
    return exception.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 오류 발생"));
  }

  private ResponseEntity<Object> handleExceptionInternalConstraint(
      ConstraintViolationException exception,
      String exceptionMessage,
      WebRequest request
  ) {

    CustomResponse<Object> body = new CustomResponse<>(exceptionMessage);
    return super.handleExceptionInternal(
        exception,
        body,
        HttpHeaders.EMPTY,
        ExceptionType.BAD_REQUEST.getHttpStatus(),
        request
    );
  }

  private ResponseEntity<Object> handleExceptionInternal(
      Exception exception,
      WebRequest request,
      String errorPoint
  ) {

    CustomResponse<String> body = CustomResponse.onFailure(
        ExceptionType.INTERNAL_SERVER_ERROR.getReason(),
        errorPoint
    );
    return super.handleExceptionInternal(
        exception,
        body,
        HttpHeaders.EMPTY,
        ExceptionType.INTERNAL_SERVER_ERROR.getHttpStatus(),
        request
    );
  }
}
