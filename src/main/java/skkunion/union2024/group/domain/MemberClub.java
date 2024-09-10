package skkunion.union2024.group.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import skkunion.union2024.member.domain.Member;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
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

}
