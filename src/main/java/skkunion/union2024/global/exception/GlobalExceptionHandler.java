package skkunion.union2024.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import skkunion.union2024.global.exception.response.ExceptionResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse> handleAuthException(final AuthException e) {

        return ResponseEntity.status(e.getHttpStatus())
                             .body(new ExceptionResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<ExceptionResponse> handleEmailVerificationException(final EmailVerificationException e) {

        return ResponseEntity.status(e.getHttpStatus())
                .body(new ExceptionResponse(e.getCode(), e.getMessage()));
    }

}
