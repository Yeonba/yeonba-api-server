package yeonba.be.exception;

import jakarta.servlet.http.HttpServletRequest;
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
}
