package skkunion.union2024.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.auth.domain.Session;
import skkunion.union2024.auth.domain.repository.SessionRepository;
import skkunion.union2024.auth.util.TokenHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionRepository sessionRepository;
    private final TokenHandler tokenHandler;

    @Transactional
    public void storeRefreshToken(String refreshToken) {
        Long memberId = extractMemberIdFrom(refreshToken);
        Session findSession = sessionRepository.findByMemberId(memberId);

        if (findSession == null)
            sessionRepository.save(Session.of(memberId, refreshToken));
        else
            findSession.updateRefreshToken(refreshToken);
    }

    @Transactional
    public void blackSessionBy(Long memberId) {
        Session findSession = sessionRepository.findByMemberId(memberId);
        if (findSession == null)
            return;

        findSession.blackSession();
    }

    private Long extractMemberIdFrom(String refreshToken) {
        return Long.parseLong(tokenHandler.getClaims(refreshToken).get("memberId").toString());
    }

}
