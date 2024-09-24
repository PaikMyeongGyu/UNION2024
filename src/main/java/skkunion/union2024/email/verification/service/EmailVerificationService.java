package skkunion.union2024.email.verification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.email.verification.config.EmailConfig;
import skkunion.union2024.email.verification.domain.EmailVerification;
import skkunion.union2024.email.verification.domain.repository.EmailVerificationRepository;
import skkunion.union2024.global.exception.AuthException;
import skkunion.union2024.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.Map;

import static jakarta.mail.Message.RecipientType.TO;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.ACCOUNT_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    public static final int EXPIRED_MINUTE = 10;
    public static final int TOKEN_LENGTH = 10;

    private final EmailVerificationRepository emailAuthRepository;
    private final MemberService memberService;
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
    public void sendEmailVerificationMessageWithRetry(int cnt, String email, String token) {
        if (cnt == 0) {
            log.error("This email({}) Verification is Failed", email);
            return;
        }

        try {
            sendEmailVerificationMessage(email, token);
        } catch(MailSendException e) {
            Map<Object, Exception> failedMessages = e.getFailedMessages();
            for (Map.Entry<Object, Exception> entry : failedMessages.entrySet()) {
                Exception cause = entry.getValue();
                if (cause instanceof MailConnectException) {
                    // 포트 문제 timeout 문제 처리
                    sendEmailVerificationMessageWithRetry(cnt-1, email, token);
                    log.error("retry: cnt={}", cnt);
                }
                else if (cause instanceof MessagingException) {
                    // 이상한 메일을 쓰면 여기로 들어옴.
                    // 대신 반송을 했을 때, 문제를 체크할 방법은 없음. --> 스케줄링을 통해 해결
                    SendFailedException sfe = (SendFailedException) cause;
                    if (sfe.getMessage().equals("Invalid Addresses")) {
                        blockInvalidEmail(email);
                    }
                    log.error("SMTP Error Code: {}", sfe.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Async("emailExecutor")
    protected void sendEmailVerificationMessage(String toEmail, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(EmailConfig.EMAIL_ID);
        message.setRecipients(TO, toEmail);
        message.setSubject(EmailConfig.SUBJECT);
        message.setText(EmailConfig.AUTH_URL + token);
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

    @Transactional
    protected void blockInvalidEmail(String email) {
        memberService.completeDelete(email);
    }
}
