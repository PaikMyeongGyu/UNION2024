package skkunion.union2024.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint( columnNames = {"email"})
        },
        indexes = @Index(name = "idx_session_email", columnList = "email")
)
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false, length = 40)
    private String email;
    private Boolean isBlackList;

    public Session(String refreshToken, String email) {
        this.refreshToken = refreshToken;
        this.email = email;
        this.isBlackList = FALSE;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void blackSession() {
        isBlackList = TRUE;
    }
}
