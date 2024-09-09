package skkunion.union2024.board.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import skkunion.union2024.group.domain.Club;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class ClubBoard {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "club_board_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 20)
    private String writerName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long likes;

    public ClubBoard(String title, Long memberId, String writerName, String content) {
        this.title = title;
        this.memberId = memberId;
        this.writerName = writerName;
        this.content = content;
    }

    public static ClubBoard of(String title, Long memberId, String writerName, String content) {
        return new ClubBoard(title, memberId, writerName, content);
    }
}
