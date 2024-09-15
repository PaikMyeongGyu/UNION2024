package skkunion.union2024.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import skkunion.union2024.global.exception.exceptioncode.ExceptionCode;

@Getter
public class ClubException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public ClubException(ExceptionCode exceptionCode) {
        this.httpStatus = exceptionCode.getHttpStatus();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
