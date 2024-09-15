package skkunion.union2024.club.common.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skkunion.union2024.club.common.domain.Club;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findClubByClubName(String clubName);
}
