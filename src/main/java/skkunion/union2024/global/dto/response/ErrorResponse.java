package skkunion.union2024.global.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        HttpStatus code,
        String message) {
}
