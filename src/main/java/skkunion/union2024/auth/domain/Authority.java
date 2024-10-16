package skkunion.union2024.auth.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static skkunion.union2024.auth.domain.AuthState.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "authority", indexes = {
        @Index(name = "idx_authority_member_id", columnList = "memberId")
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
