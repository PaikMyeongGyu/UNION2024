package skkunion.union2024.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skkunion.union2024.auth.domain.Authority;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    List<Authority> findAllByMemberId(Long memberId);
}
