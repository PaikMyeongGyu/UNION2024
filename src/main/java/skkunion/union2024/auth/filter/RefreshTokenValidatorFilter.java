package skkunion.union2024.auth.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import skkunion.union2024.auth.domain.AuthTokenContext;
import skkunion.union2024.auth.domain.Authority;
import skkunion.union2024.auth.domain.Session;
import skkunion.union2024.auth.domain.repository.AuthorityRepository;
import skkunion.union2024.auth.domain.repository.SessionRepository;
import skkunion.union2024.auth.util.TokenHandler;
import skkunion.union2024.global.exception.AuthException;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import java.io.IOException;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;
import static skkunion.union2024.member.domain.MemberState.ACTIVE;

@RequiredArgsConstructor
public class RefreshTokenValidatorFilter extends OncePerRequestFilter {

    private final AuthTokenContext authTokenContext;
    private final TokenHandler tokenHandler;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String refreshToken = request.getHeader(authTokenContext.getAuthHeader());
            if (refreshToken == null)
                throw new BadCredentialsException("다시 로그인해주세요.");

            Claims claims = tokenHandler.getClaims(refreshToken);

            Long memberId = Long.parseLong(claims.get("memberId").toString());
            Session findSession = sessionRepository.findByMemberId(memberId);
            if (findSession == null)
                throw new BadCredentialsException("다시 로그인해주세요.");

            isBlackList(findSession);
            isCurrentToken(findSession, refreshToken);

            Member findMember = memberRepository.findById(memberId)
                                                .orElseThrow(() -> new AuthException(ACCOUNT_NOT_FOUND));

            // 삭제상태인지 확인
            if (findMember.getStatus() == ACTIVE) {
                List<Authority> findAuthorities = authorityRepository.findAllByMemberId(memberId);
                var auth = new UsernamePasswordAuthenticationToken(findMember.getEmail(), null,
                                AuthorityUtils.commaSeparatedStringToAuthorityList(findAuthorities.toString()));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new AuthException(ACCOUNT_NOT_FOUND);
            }

        } catch (ExpiredJwtException | BadCredentialsException e) {
            tokenHandler.handleException(response, REQUEST_LOGIN);
            return;
        } catch (SignatureException e) {
            tokenHandler.handleException(response, INVALID_REQUEST);
            return;
        } catch (AuthException e) {
            tokenHandler.handleException(response, ACCOUNT_NOT_FOUND);
        }

        filterChain.doFilter(request, response);
    }

    // 재발급 상황에서만 사용함.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().equals("/reissue");
    }

    private static void isCurrentToken(Session findSession, String refreshToken) {
        if (!findSession.getRefreshToken().equals(refreshToken))
            throw new BadCredentialsException("다시 로그인 해주세요.");
    }

    private static void isBlackList(Session findSession) {
        if (findSession.getIsBlackList() == TRUE)
            throw new BadCredentialsException("다시 로그인 해주세요.");
    }

}
