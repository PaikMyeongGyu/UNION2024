package skkunion.union2024.emailVerification.domain.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.emailVerification.domain.EmailVerification;
import skkunion.union2024.emailVerification.domain.config.EmailConfig;
import skkunion.union2024.emailVerification.domain.repository.EmailVerificationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.*;
import static skkunion.union2024.emailVerification.domain.config.EmailConfig.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationService {
    public static final int EXPIRED_MINUTE = 10;
    public static final int TOKEN_LENGTH = 10;

    private final EmailVerificationRepository emailAuthRepository;
    private final JavaMailSender mailSender;

    public void createTemporaryEmailAuth(String email, String token, LocalDateTime expiredTime) {
        if (isAlreadyExists(email)) {
            updateTemporaryEmailAuth(email, expiredTime);
            return;
        }

        createNewTemporaryEmailAuth(email, token, expiredTime);
    }

    public void createTemporaryEmailAuth(String email, String token) {
        LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(EXPIRED_MINUTE);
        createTemporaryEmailAuth(email, token, expiredTime);
    }

    public void sendEmailVerificationMessage(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_ID);
        message.setTo(toEmail);
        message.setSubject(SUBJECT);
        message.setText(AUTH_URL + token);
        mailSender.send(message);
    }

    public void deleteEmailAuth(String email) {
        if (isAlreadyExists(email)) {
            emailAuthRepository.deleteByEmail(email);
            return;
        }

        throw new EntityNotFoundException("EmailAuth가 존재하지 않습니다.");
    }

    public EmailVerification findEmailVerificationByToken(String token) {
        return emailAuthRepository.findByToken(token);
    }

    private void createNewTemporaryEmailAuth(String email, String token, LocalDateTime expiredTime) {
        EmailVerification emailAuth = new EmailVerification(email, token, expiredTime);
        emailAuthRepository.save(emailAuth);
    }

    private void updateTemporaryEmailAuth(String email, LocalDateTime expiredTime) {
        String token = randomAlphanumeric(TOKEN_LENGTH);
        emailAuthRepository.refreshEmailVerificationToken(email, token, expiredTime);
    }

    private boolean isAlreadyExists(String email) {
        return emailAuthRepository.existsByEmail(email);
    }
}
