package skkunion.union2024.account.service;

import static java.time.LocalDateTime.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static skkunion.union2024.email.verification.service.EmailVerificationService.TOKEN_LENGTH;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.email.verification.domain.EmailVerification;
import skkunion.union2024.email.verification.service.EmailVerificationService;
import skkunion.union2024.global.exception.EmailVerificationException;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceFacade {

    private final EmailVerificationService emailVerificationService;
    private final MemberService memberService;

    public void createAccountWithEmailVerification(String nickname, String email, String password) {
        joinMember(nickname, email, password);

        String token = randomAlphanumeric(TOKEN_LENGTH);
        emailVerificationService.sendEmailVerification(email, token);

        emailVerificationService.createTemporaryEmailAuth(email, token);
    }

    @Transactional
    public void deleteAccount(String email, String password) {
        Member findMember = findMemberBy(email);

        // 멤버 삭제 규칙 --> 한번 요청하고 패스워드 작성하게 할 것.
        if (!memberService.IsPasswordMatch(findMember,password))
            throw new EmailVerificationException(ACCOUNT_INFO_DOES_NOT_MATCH);
        memberService.deleteMemberById(findMember.getId());
    }

    @Transactional
    public void resendEmailVerification(String email, String password) {
        Member findMember = findMemberBy(email);
        isMemberActivated(findMember);
        checkIsPasswordMatch(password, findMember);

        String token = randomAlphanumeric(TOKEN_LENGTH);
        emailVerificationService.sendEmailVerification(email, token);
        emailVerificationService.refreshTemporaryEmailVerification(email, token);
    }

    @Transactional
    public void tryEmailVerification(String token) {
        var findEmailVerification = emailVerificationService.findEmailVerificationByToken(token);
        emailVerificationValidation(findEmailVerification);

        memberService.activateMemberByEmail(findEmailVerification.getEmail());
        emailVerificationService.deleteEmailAuth(findEmailVerification.getEmail());
    }

    private void emailVerificationValidation(EmailVerification findEmailVerification) {
        if (findEmailVerification == null)
            throw new EmailVerificationException(EMAIL_VERIFICATION_NOT_FOUND);

        if (findEmailVerification.isExpired(now()))
            throw new EmailVerificationException(EMAIL_VERIFICATION_EXPIRED);
    }

    private void joinMember(String nickname, String email, String password) {
        try {
            memberService.join(nickname, email, password);
        } catch (ConstraintViolationException e) {
            throw new EmailVerificationException(ACCOUNT_ALREADY_EXIST);
        }
    }

    private void checkIsPasswordMatch(String password, Member findMember) {
        if (!memberService.IsPasswordMatch(findMember, password)) {
            throw new EmailVerificationException(INVALID_REQUEST);
        }
    }

    private void isMemberActivated(Member findMember) {
        if (findMember.isActivated()) {
            throw new EmailVerificationException(INVALID_REQUEST);
        }
    }

    private Member findMemberBy(String email) {
        return memberService.findMemberByEmail(email)
                .orElseThrow(() -> new EmailVerificationException(ACCOUNT_NOT_FOUND));
    }

}
