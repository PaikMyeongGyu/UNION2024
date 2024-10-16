package skkunion.union2024.club.common.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.member.domain.Member;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByClubAndMember(Club club, Member member);
    Optional<ClubMember> findByClubAndNickName(Club club, String nickName);
}
