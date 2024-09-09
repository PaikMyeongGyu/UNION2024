package skkunion.union2024.auth.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import skkunion.union2024.auth.dto.TokenResponse;
import skkunion.union2024.global.exception.exceptioncode.ExceptionCode;
import skkunion.union2024.global.exception.response.ErrorResponse;

import javax.crypto.SecretKey;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static io.jsonwebtoken.lang.Strings.UTF_8;

/**
 * 생성 시점은 Authentication 과정에서 AccessToken과 RefreshToken을 필터에서 생성하는 시점임.
 * ThreadLocal을 사용해서 필터에서 생성된 토큰 쌍을 Controller에서 받아 바디에 담아 내보냄.
 * 반드시 ThreadLocal로 사용된 데이턴 Controller에서 응답을 준 뒤 삭제할 것 !!!!
 */
@Getter
@Component
public class AuthTokenContext {

    private static final ThreadLocal<TokenResponse> currentToken = new ThreadLocal<>();

    private final String authHeader;
    private final SecretKey secretKey;

    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public AuthTokenContext(
            @Value("#{environment['app.secret-key']}") String secretKey,
            @Value("#{environment['app.auth-header']}") String authHeader
    ) {
        this.authHeader = authHeader;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8));
    }

    public void storeCurrentToken(String accessToken, String refreshToken) {
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        currentToken.set(tokenResponse);
    }

    public TokenResponse getCurrentToken() {
        return currentToken.get();
    }

    public void clearToken() {
        currentToken.remove();
    }
}