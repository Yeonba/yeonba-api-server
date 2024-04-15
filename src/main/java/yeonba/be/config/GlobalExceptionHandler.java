package yeonba.be.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yeonba.be.util.CustomResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse<Void>> handleIllegalArgumentException(
        IllegalArgumentException e) {

        return ResponseEntity
            .badRequest()
            .body(new CustomResponse<>(e.getMessage()));
    }

}
