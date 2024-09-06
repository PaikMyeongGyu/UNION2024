package skkunion.union2024.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skkunion.union2024.auth.domain.AuthTokenContext;
import skkunion.union2024.auth.dto.TokenResponse;
import skkunion.union2024.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthTokenContext authTokenContext;

    @RequestMapping("/login")
    public ResponseEntity<TokenResponse> loginMember() {
        TokenResponse tokens = authTokenContext.getCurrentToken();
        authService.storeRefreshToken(tokens.refreshToken());
        authTokenContext.clearToken();
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueExpiredToken() {
        TokenResponse tokens = authTokenContext.getCurrentToken();
        authTokenContext.clearToken();
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutMember(Authentication authentication) {
        String email = getEmailFrom(authentication);
        authService.blackSessionBy(email);
        return ResponseEntity.noContent().build();
    }

    private static String getEmailFrom(Authentication authentication) {
        return authentication.getName();
    }
}
