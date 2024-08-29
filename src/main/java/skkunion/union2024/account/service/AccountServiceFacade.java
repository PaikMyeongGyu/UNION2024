package skkunion.union2024.account.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.emailVerification.domain.EmailVerification;
import skkunion.union2024.emailVerification.domain.service.EmailVerificationService;
import skkunion.union2024.global.exception.EmailVerificationExpiredException;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static skkunion.union2024.emailVerification.domain.service.EmailVerificationService.TOKEN_LENGTH;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceFacade {

    private final EmailVerificationService emailVerificationService;
    private final MemberService memberService;

    @Transactional
    public void createAccountWithEmailVerification(String nickname, String email, String password) {
        if (memberService.isMemberExist(email))
            throw new EntityExistsException(email);

        memberService.joinMember(new Member(nickname, email, password));

        String token = randomAlphanumeric(TOKEN_LENGTH);
        emailVerificationService.sendEmailVerificationMessage(email, token);
        emailVerificationService.createTemporaryEmailAuth(email, token);
    }

    @Transactional
    public void tryEmailVerification(String token) {
        var findEmailVerification = emailVerificationService.findEmailVerificationByToken(token);
        if (findEmailVerification == null)
            throw new EntityNotFoundException("존재하는 인증정보가 없습니다.");

        if (findEmailVerification.isExpired(now()))
            throw new EmailVerificationExpiredException(token);

        memberService.activateMember(findEmailVerification.getEmail());
    }
}
