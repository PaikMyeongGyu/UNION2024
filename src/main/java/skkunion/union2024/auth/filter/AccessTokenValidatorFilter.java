package skkunion.union2024.auth.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import skkunion.union2024.auth.domain.AuthTokenContext;
import skkunion.union2024.auth.util.TokenHandler;

import java.io.IOException;

import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

@RequiredArgsConstructor
public class AccessTokenValidatorFilter extends OncePerRequestFilter {

    private final AuthTokenContext authTokenContext;
    private final TokenHandler tokenHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = request.getHeader(authTokenContext.getAuthHeader());

        if (accessToken != null) {
            try {
                Claims claims = tokenHandler.getClaims(accessToken);
                String username = String.valueOf(claims.get("username"));
                String authorities = String.valueOf(claims.get("authorities"));

                tokenHandler.createAuthenticationContext(username, authorities);
            } catch (ExpiredJwtException e) {
                tokenHandler.handleException(response, ACCESS_TOKEN_EXPIRED);
                return;
            } catch(SignatureException e) {
                tokenHandler.handleException(response, INVALID_REQUEST);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 회원 가입, 로그인, 재발급 땐 제외함.
     * --> 게스트에 대해선 어떻게 해야되지??? 표현이 굉장히 헷갈리는데... 조금 리팩토링이 필요함.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/auth/login") || path.equals("/accounts") || path.equals("/auth/reissue");
    }
}
