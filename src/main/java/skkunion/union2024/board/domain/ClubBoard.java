package skkunion.union2024.board.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ClubBoard {

    @Getter
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "club_board_id")
    private Long id;

    @Getter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_member_id")
    private ClubMember clubMember;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long likes;

    @Getter
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @Builder
    public ClubBoard(String title, String content, Club club, ClubMember clubMember, String memberEmail, String nickname) {
        this.title = title;
        this.content = content;
        this.club = club;
        this.clubMember = clubMember;
        this.memberEmail = memberEmail;
        this.nickname = nickname;
        this.likes = 0L;
    }
}
