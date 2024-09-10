package skkunion.union2024.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.auth.domain.Session;
import skkunion.union2024.auth.domain.repository.SessionRepository;
import skkunion.union2024.auth.util.TokenHandler;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionRepository sessionRepository;
    private final TokenHandler tokenHandler;

    @Transactional
    public void storeRefreshToken(String refreshToken) {
        String email = extractEmailFrom(refreshToken);
        Session findSession = sessionRepository.findByEmail(email);

        if (findSession == null)
            sessionRepository.save(new Session(refreshToken, email));
        else
            findSession.updateRefreshToken(refreshToken);
    }

    @Transactional
    public void blackSessionBy(String email) {
        Session findSession = sessionRepository.findByEmail(email);

        if (findSession == null)
            return;

        findSession.blackSession();
    }

    private String extractEmailFrom(String refreshToken) {
        return tokenHandler.getClaims(refreshToken).get("username").toString();
    }

}
