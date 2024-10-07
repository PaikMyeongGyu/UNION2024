package skkunion.union2024.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skkunion.union2024.auth.domain.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByMemberId(Long memberId);
}