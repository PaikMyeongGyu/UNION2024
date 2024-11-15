package skkunion.union2024.email.verification.service;

import static jakarta.mail.Message.RecipientType.TO;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;

import java.time.LocalDateTime;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.email.verification.config.EmailConfig;
import skkunion.union2024.email.verification.domain.EmailVerification;
import skkunion.union2024.email.verification.domain.repository.EmailVerificationRepository;
import skkunion.union2024.global.exception.AuthException;
import skkunion.union2024.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    @Value("${app.email}")
    private String EMAIL_ID;

    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailSendRecordService emailSendRecordService;
    private final MemberService memberService;
    private final JavaMailSender mailSender;

    public static final int EXPIRED_MINUTE = 10;
    public static final int TOKEN_LENGTH = 10;

    @Transactional
    public void createTemporaryEmailAuth(String email, String token) {
        LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(EXPIRED_MINUTE);
        createNewTemporaryEmailVerification(email, token, expiredTime);
    }

    @Transactional
    public void refreshTemporaryEmailVerification(String email, String token) {
        emailVerificationRepository
                .refreshEmailVerificationToken(email, token, LocalDateTime.now().plusMinutes(EXPIRED_MINUTE));
    }

    @Transactional
    public void deleteEmailAuth(String email) {
        if (isAlreadyExists(email)) {
            emailVerificationRepository.deleteByEmail(email);
            emailSendRecordService.deleteRecordByEmail(email);
            return;
        }
        throw new AuthException(ACCOUNT_NOT_FOUND);
    }

    @Async("emailExecutor")
    public void sendEmailVerification(String email, String token) {
        try {
            sendEmailVerificationMessage(email, token);
        } catch (Exception e) {
            log.error("{}: 이메일 요청 실패", email);
        } finally {
            emailSendRecordService.recordSaveOrIncreaseCount(email);
        }
    }

    @Async("emailExecutor")
    protected void sendEmailVerificationMessage(String toEmail, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(EMAIL_ID);
        message.setRecipients(TO, toEmail);
        message.setSubject(EmailConfig.SUBJECT);
        message.setText(generateMessage(EmailConfig.AUTH_URL + token));
        mailSender.send(message);
    }

    public EmailVerification findEmailVerificationByToken(String token) {
        return emailVerificationRepository.findByToken(token);
    }

    private void createNewTemporaryEmailVerification(String email, String token, LocalDateTime expiredTime) {
        EmailVerification emailAuth = new EmailVerification(email, token, expiredTime);
        emailVerificationRepository.save(emailAuth);
    }

    private boolean isAlreadyExists(String email) {
        return emailVerificationRepository.existsByEmail(email);
    }

    private void blockInvalidEmailByMemberId(Long memberId) {
        memberService.completeDeleteById(memberId);
    }

    private String generateMessage(String token) {
        String message = "<h1>Email Verification</h1>";
        message += "<p>Click the button below to verify your email:</p>";
        message += "<a href=\"" + token + "\" style=\"display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #4CAF50; text-align: center; text-decoration: none; border-radius: 5px;\">Verify Email</a>";
        return message;
    }
}
