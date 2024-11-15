package skkunion.union2024.email.verification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.email.verification.domain.EmailSendRecord;

import java.util.Optional;

public interface EmailSendRecordRepository extends JpaRepository<EmailSendRecord, Long> {
    Optional<EmailSendRecord> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("update EmailSendRecord set count = count + 1 where email = :email")
    void increaseCount(String email);

    void deleteByEmail(String email);
}
