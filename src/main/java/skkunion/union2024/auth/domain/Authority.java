package skkunion.union2024.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static skkunion.union2024.auth.domain.AuthState.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "authority", indexes = {
        @Index(name = "idx_authority_email", columnList = "email")
})
public class Authority {

    private static final Long GUEST_ID = 1L;
    private static final Authority GUEST = new Authority(GUEST_ID, AuthState.GUEST.getRole());

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 30)
    private String role;

    public Authority(Long memberId, String role) {
        this.memberId = memberId;
        this.role = role;
    }

    public Authority Admin(Long memberId) {
        return new Authority(memberId, ADMIN.getRole());
    }

    public Authority User(Long memberId) {
        return new Authority(memberId, USER.getRole());
    }
}
