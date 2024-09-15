package skkunion.union2024.club.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import skkunion.union2024.board.domain.ClubBoard;
import skkunion.union2024.member.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


/**
 * 단순 해당 테이블에 대한 설계 의도는 member랑 club 정보를 확실하게 가져오고 그 과정에서 권한 체크를 위함임
 * MySQL에서는 자동으로 Foreign Key를 생성하고 조작할 수 없기 때문에 해당 키는 이런 용도로 사용될 것 같음.
 *
 * --> member의 경우, member가 club을 찾는 경우, 이 경우에는 별도의 개수 제한 정책을 두면 레코드 개수가 적을 거라
 *     파일 소트해도 상관없을 것 같아서 추가 인덱스 필요 X
 *
 * --> club은 club이 member를 찾는 경우, 아마도 club의 경우에는 멤버를 찾고 하는데 이 때는 조금 관리자 입장에서
 *     member
 */
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "member_club", indexes = {
        @Index(name = "idx_club_member", columnList = "club_id, member_id"),
        @Index(name = "idx_club_authority", columnList = "club_id, club_authority")
})
public class MemberClub {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_club_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "memberClub", cascade = PERSIST, fetch = LAZY)
    private List<ClubBoard> clubBoards = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ClubAuthority clubAuthority;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

}
