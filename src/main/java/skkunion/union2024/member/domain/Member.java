package skkunion.union2024.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import skkunion.union2024.club.common.domain.ClubMember;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;
import static skkunion.union2024.member.domain.MemberState.ACTIVE;
import static skkunion.union2024.member.domain.MemberState.UNVERIFIED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE member SET status = 'DELETED' WHERE id = ?")
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = PERSIST, fetch = LAZY)
    private List<ClubMember> memberClubs = new ArrayList<>();

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, unique = true, length = 40)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime lastLoginDate;

    @Enumerated(EnumType.STRING)
    private MemberState status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public Member(final Long id, final String nickname, final String email, final String password) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.lastLoginDate = LocalDateTime.now();
        this.status = UNVERIFIED;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public Member(final String nickname, final String email, final String password) {
        this(null, nickname, email, password);
    }

    public boolean isActivated() {
        return status == ACTIVE;
    }
}
