package skkunion.union2024.auth.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import skkunion.union2024.auth.domain.AuthTokenContext;
import skkunion.union2024.auth.dto.TokenResponse;
import skkunion.union2024.auth.service.AuthService;
import skkunion.union2024.global.annotation.AuthMember;
import skkunion.union2024.member.dto.AuthMemberDto;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<Void> logoutMember(
            @AuthMember AuthMemberDto authMemberDto) {
        Long memberId = authMemberDto.memberId();
        authService.blackSessionBy(memberId);
        return ResponseEntity.noContent().build();
    }

}
