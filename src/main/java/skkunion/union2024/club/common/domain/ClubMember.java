package skkunion.union2024.club.common.domain;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static skkunion.union2024.club.common.domain.ClubAuthority.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import skkunion.union2024.board.domain.ClubBoard;
import skkunion.union2024.member.domain.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "club_member", indexes = {
        @Index(name = "idx_club_member", columnList = "slug, member_id"),
        @Index(name = "idx_club_authority", columnList = "slug, club_authority, club_member_id desc"),
        @Index(name = "idx_club_nick_name", columnList = "nick_name")
})
public class ClubMember implements Comparable<ClubMember> {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "slug")
    private Club club;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "clubMember", cascade = PERSIST, fetch = LAZY)
    private List<ClubBoard> clubBoards = new ArrayList<>();

    @Getter
    @Column(nullable = false, length = 20)
    private String nickName;

    @Getter
    @Enumerated(STRING)
    private ClubAuthority clubAuthority;

    @Getter
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    public ClubMember(Club club, Member member, String nickName, ClubAuthority clubAuthority) {
        this.club = club;
        this.nickName = nickName;
        this.member = member;
        this.clubAuthority = clubAuthority;
    }

    public static ClubMember President(Club club, Member member, String nickName) {
        return new ClubMember(club, member, nickName, PRESIDENT);
    }

    public static ClubMember Manager(Club club, Member member, String nickName) {
        return new ClubMember(club, member, nickName, VICE_PRESIDENT);
    }

    public static ClubMember General(Club club, Member member, String nickName) {
        return new ClubMember(club, member, nickName, GENERAL);
    }

    @Override
    public int compareTo(ClubMember o) {
        if (clubAuthority == o.clubAuthority) {
            return id.compareTo(o.id);
        } else {
            return clubAuthority.compareTo(o.clubAuthority);
        }
    }
}
