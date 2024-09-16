package skkunion.union2024.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.emailVerification.service.EmailVerificationService;
import skkunion.union2024.global.exception.EmailVerificationException;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static skkunion.union2024.emailVerification.service.EmailVerificationService.TOKEN_LENGTH;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceTempFacade {

    private final EmailVerificationService emailVerificationService;
    private final MemberService memberService;

    @Transactional
    public void createAccountWithEmailVerification(String nickname, String email, String password) {
        if (memberService.isMemberExistWith(email))
            throw new EmailVerificationException(ACCOUNT_ALREADY_EXIST);

        memberService.join(nickname, email, password);

        String token = randomAlphanumeric(TOKEN_LENGTH);
        emailVerificationService.createTemporaryEmailAuth(email, token);
        var findEmailVerification = emailVerificationService.findEmailVerificationByToken(token);
        memberService.activateMember(findEmailVerification.getEmail());
        emailVerificationService.deleteEmailAuth(findEmailVerification.getEmail());
    }

    @Transactional
    public void deleteAccount(String email, String password) {
        Member findMember = memberService.findMemberBy(email)
                             .orElseThrow(() -> new EmailVerificationException(ACCOUNT_NOT_FOUND));

        // 멤버 삭제 규칙 --> 한번 요청하고 패스워드 작성하게 할 것.
        if (!memberService.memberPasswordIsMatch(findMember,password))
            throw new EmailVerificationException(ACCOUNT_INFO_DOES_NOT_MATCH);

        memberService.deleteBy(email);
    }

    public void resendEmailVerification(String email, String password) {
        Member findMember = memberService.findMemberBy(email)
                            .orElseThrow(() -> new EmailVerificationException(ACCOUNT_NOT_FOUND));

        if (findMember.isActivated())
            throw new EmailVerificationException(INVALID_REQUEST);

        if (!memberService.memberPasswordIsMatch(findMember, password))
            throw new EmailVerificationException(ACCOUNT_INFO_DOES_NOT_MATCH);

        String token = randomAlphanumeric(TOKEN_LENGTH);
        emailVerificationService.sendEmailVerificationMessage(email, token);
        emailVerificationService.refreshTemporaryEmailVerification(email, token);
    }

    @Transactional
    public void tryEmailVerification(String token) {
        var findEmailVerification = emailVerificationService.findEmailVerificationByToken(token);
        if (findEmailVerification == null)
            throw new EmailVerificationException(EMAIL_VERIFICATION_NOT_FOUND);

        if (findEmailVerification.isExpired(now()))
            throw new EmailVerificationException(EMAIL_VERIFICATION_EXPIRED);

        memberService.activateMember(findEmailVerification.getEmail());
        emailVerificationService.deleteEmailAuth(findEmailVerification.getEmail());
    }
}
