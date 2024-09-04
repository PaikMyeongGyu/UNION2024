package skkunion.union2024.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Entity
@Getter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;
    private Boolean isBlackList;

    public Session(String refreshToken) {
        this.refreshToken = refreshToken;
        this.isBlackList = FALSE;
    }

    public void blackSession() {
        isBlackList = TRUE;
    }
}
