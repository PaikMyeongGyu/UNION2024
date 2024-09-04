package skkunion.union2024.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
