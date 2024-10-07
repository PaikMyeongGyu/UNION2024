package skkunion.union2024.club.common.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.member.domain.Member;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByClubAndMember(Club club, Member member);
    Optional<ClubMember> findByNickName(String nickName);
}
