package skkunion.union2024.emailVerification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import skkunion.union2024.emailVerification.domain.EmailVerification;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmail(String email);
    EmailVerification findByToken(String token);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    void deleteByToken(String token);


    @Modifying
    @Query("""
            UPDATE EmailVerification emailVerification
            SET emailVerification.token = :token, emailVerification.expiredAt = :expiredAt
            WHERE emailVerification.email = :email
            """)
    void refreshEmailVerificationToken(
            @Param("email") final String email,
            @Param("token") final String token,
            @Param("expiredAt") final LocalDateTime expiredAt
    );

}
