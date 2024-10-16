package skkunion.union2024.auth.filter;

import static skkunion.union2024.auth.domain.AuthTokenContext.ACCESS_TOKEN_EXPIRATION_TIME;
import static skkunion.union2024.auth.domain.AuthTokenContext.REFRESH_TOKEN_EXPIRATION_TIME;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;

import java.io.IOException;
import java.util.Date;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import skkunion.union2024.auth.domain.AuthTokenContext;
import skkunion.union2024.auth.util.TokenHandler;
import skkunion.union2024.global.exception.AuthException;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthTokenGenerateFilter extends OncePerRequestFilter {

    private final AuthTokenContext tokenContext;
    private final TokenHandler tokenHandler;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String path = request.getRequestURI();

        if (authentication != null) {
            if (path.equals("/login")) {
                String accessToken = generateAccessTokenLogin(authentication);
                String refreshToken = generateRefreshTokenLogin(authentication);
                tokenContext.storeCurrentToken(accessToken, refreshToken);
            } else {
                String accessToken = generateAccessTokenReissue(authentication);
                String refreshToken = generateRefreshTokenReissue(authentication);
                tokenContext.storeCurrentToken(accessToken, refreshToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 로그인과 재발급 때를 제외하고는 여기를 접근하면 안된다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.equals("/login") || path.equals("/reissue"));
    }

    private String generateAccessTokenLogin(Authentication authentication) {
        Member findMember = memberRepository.findByEmail(authentication.getName())
                                            .orElseThrow(() -> new AuthException(ACCOUNT_NOT_FOUND));

        return Jwts.builder()
                .claim("memberId", findMember.getId())
                .claim("authorities", tokenHandler.mergeAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(tokenContext.getSecretKey()).compact();
    }

    private String generateAccessTokenReissue(Authentication authentication) {
        return Jwts.builder()
                .claim("memberId", authentication.getName())
                .claim("authorities", tokenHandler.mergeAuthorities(authentication.getAuthorities()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(tokenContext.getSecretKey()).compact();
    }

    private String generateRefreshTokenLogin(Authentication authentication) {
        Member findMember = memberRepository.findByEmail(authentication.getName())
                                            .orElseThrow(() -> new AuthException(ACCOUNT_NOT_FOUND));

        return Jwts.builder()
                .claim("memberId", findMember.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(tokenContext.getSecretKey()).compact();
    }

    private String generateRefreshTokenReissue(Authentication authentication) {
        return Jwts.builder()
                .claim("memberId", authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(tokenContext.getSecretKey()).compact();
    }
}
