package skkunion.union2024.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(String nickname, String email, String password) {
        memberRepository.save(new Member(nickname, email, passwordEncoder.encode(password)));
    }

    public void deleteBy(String email) {
        memberRepository.deleteByMemberEmail(email);
    }

    public Optional<Member> findMemberBy(String email) {
        return memberRepository.findByEmail(email);
    }

    public boolean memberPasswordIsMatch(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    public boolean isMemberExistWith(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void activateMember(String email) {
        memberRepository.activateMemberByEmail(email);
    }
}
