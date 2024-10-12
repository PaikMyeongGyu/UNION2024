package skkunion.union2024.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.global.exception.EmailVerificationException;
import skkunion.union2024.global.exception.exceptioncode.ExceptionCode;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(String nickname, String email, String password) {
        memberRepository.save(new Member(nickname, email, passwordEncoder.encode(password)));
    }

    @Transactional
    public void deleteMemberById(Long memberId) {
        memberRepository.deleteSoftById(memberId);
    }

    @Transactional
    public void completeDelete(Long memberId) {
        Member findMember = findMemberById(memberId).orElseThrow(()
                -> new EmailVerificationException(ExceptionCode.ACCOUNT_NOT_FOUND));

        memberRepository.deleteCompleteById(findMember.getId());
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public boolean IsPasswordMatch(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    public boolean isMemberExistWithEmail(String email) {
        return memberRepository.existsMemberByEmail(email);
    }

    public void activateMemberByEmail(String email) {
        memberRepository.activateMemberByEmail(email);
    }
}
