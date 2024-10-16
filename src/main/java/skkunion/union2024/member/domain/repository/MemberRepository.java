package skkunion.union2024.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByEmail(String email);

    Optional<Member> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Member member
            SET member.status = 'DELETED'
            WHERE member.id = :memberId
            """)
    void deleteSoftById(@Param("memberId") final Long memberId);

    @Modifying
    @Query("""
            UPDATE Member member
            SET member.status = 'ACTIVE'
            WHERE member.email = :memberEmail
            """)
    void activateMemberByEmail(@Param("memberEmail") final String memberEmail);

    @Modifying
    @Query("""
        DELETE from Member member
        WHERE member.id = :memberId
        """)
    void deleteCompleteById(@Param("memberId") final Long memberId);
}
