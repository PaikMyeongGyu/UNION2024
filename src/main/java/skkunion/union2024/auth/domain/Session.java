package skkunion.union2024.auth.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint( columnNames = {"memberId"})
        },
        indexes = @Index(name = "idx_session_member_id", columnList = "memberId")
)
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private Long memberId;

    @Column(nullable = false)
    private String refreshToken;

    private Boolean isBlackList;

    public Session(Long memberId, String refreshToken) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.isBlackList = FALSE;
    }

    public static Session of( Long memberId, String refreshToken) {
        return new Session(memberId, refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void blackSession() {
        isBlackList = TRUE;
    }
}
