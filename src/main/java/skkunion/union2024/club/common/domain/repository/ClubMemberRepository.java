package skkunion.union2024.club.common.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skkunion.union2024.club.common.domain.ClubMember;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
}
