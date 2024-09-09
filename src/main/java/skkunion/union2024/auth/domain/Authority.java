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

    private static final String GUEST_ID = "Guest";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 30)
    private String role;

    public Authority(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public Authority Admin(String email) {
        return new Authority(email, ADMIN.getRole());
    }

    public Authority Guest() {
        return new Authority(GUEST_ID, GUEST.getRole());
    }

    public Authority User(String email) {
        return new Authority(email, USER.getRole());
    }
}
