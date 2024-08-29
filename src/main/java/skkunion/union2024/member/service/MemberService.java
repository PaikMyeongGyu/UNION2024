package skkunion.union2024.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * @param member
     * @return savedMember
     */
    public Member joinMember(Member member) {
        return memberRepository.save(member);
    }

    public boolean isMemberExist(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void activateMember(String email) {
        memberRepository.activateMemberByEmail(email);
    }
}
