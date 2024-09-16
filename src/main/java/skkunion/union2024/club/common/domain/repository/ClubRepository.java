package skkunion.union2024.club.common.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.club.common.domain.Club;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findClubByClubName(String clubName);
    Optional<Club> findClubBySlug(String slug);

    @Modifying
    @Transactional
    @Query(value = "UPDATE club SET total_members = total_members + 1 WHERE slug = :slug", nativeQuery = true)
    int updateTotalMembersBySlug(@Param("slug") String slug);
}
