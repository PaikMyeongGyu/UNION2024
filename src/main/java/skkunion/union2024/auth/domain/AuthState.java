package skkunion.union2024.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthState {
    ADMIN("role_admin"),
    GUEST("role_guest"),
    USER("role_user");

    private final String role;
}
