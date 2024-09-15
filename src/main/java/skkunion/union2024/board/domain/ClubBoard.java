package skkunion.union2024.board.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ClubBoard {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "club_board_id")
    private Long id;

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
    @ColumnDefault("0")
    private Long likes;

    public ClubBoard(String title, String memberEmail, String nickname, String content) {
        this.title = title;
        this.memberEmail = memberEmail;
        this.nickname = nickname;
        this.content = content;
    }

    public static ClubBoard of(String title, String memberEmail, String writerName, String content) {
        return new ClubBoard(title, memberEmail, writerName, content);
    }
}
