package skkunion.union2024.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skkunion.union2024.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("""
            UPDATE Member member
            SET member.status = 'DELETED'
            WHERE member.email = :memberEmail
            """)
    void deleteByMemberEmail(@Param("memberEmail") final String memberEmail);

    @Modifying
    @Query("""
            UPDATE Member member
            SET member.status = 'ACTIVE'
            WHERE member.email = :memberEmail
            """)
    void activateMemberByEmail(@Param("memberEmail") final String memberEmail);
}
