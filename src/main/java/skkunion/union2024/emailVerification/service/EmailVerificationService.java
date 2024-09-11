package skkunion.union2024.emailVerification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.emailVerification.domain.EmailVerification;
import skkunion.union2024.emailVerification.domain.repository.EmailVerificationRepository;
import skkunion.union2024.global.exception.AuthException;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static skkunion.union2024.emailVerification.config.EmailConfig.*;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    public static final int EXPIRED_MINUTE = 10;
    public static final int TOKEN_LENGTH = 10;

    private final EmailVerificationRepository emailAuthRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public void createTemporaryEmailVerification(String email, String token, LocalDateTime expiredTime) {
        createNewTemporaryEmailVerification(email, token, expiredTime);
    }

    @Transactional
    public void createTemporaryEmailAuth(String email, String token) {
        LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(EXPIRED_MINUTE);
        createTemporaryEmailVerification(email, token, expiredTime);
    }

    @Transactional
    public void refreshTemporaryEmailVerification(String email, String token) {
        refreshTemporaryEmailVerification(email, token, LocalDateTime.now().plusMinutes(EXPIRED_MINUTE));
    }

    public void refreshTemporaryEmailVerification(String email, String token, LocalDateTime expiredTime) {
        emailAuthRepository.refreshEmailVerificationToken(email, token, expiredTime);
    }

    @Async("emailExecutor")
    public void sendEmailVerificationMessage(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_ID);
        message.setTo(toEmail);
        message.setSubject(SUBJECT);
        message.setText(AUTH_URL + token);
        mailSender.send(message);
    }

    @Transactional
    public void deleteEmailAuth(String email) {
        if (isAlreadyExists(email)) {
            emailAuthRepository.deleteByEmail(email);
            return;
        }

        throw new AuthException(ACCOUNT_NOT_FOUND);
    }

    public EmailVerification findEmailVerificationByToken(String token) {
        return emailAuthRepository.findByToken(token);
    }

    private void createNewTemporaryEmailVerification(String email, String token, LocalDateTime expiredTime) {
        EmailVerification emailAuth = new EmailVerification(email, token, expiredTime);
        emailAuthRepository.save(emailAuth);
    }

    private boolean isAlreadyExists(String email) {
        return emailAuthRepository.existsByEmail(email);
    }
}
