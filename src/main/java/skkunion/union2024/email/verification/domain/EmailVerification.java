package skkunion.union2024.email.verification.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 10)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public EmailVerification(String email, String token, LocalDateTime expiredAt) {
        this.email = email;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public boolean isExpired(LocalDateTime time) {
        return expiredAt.isBefore(time);
    }
}
