package skkunion.union2024.like.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skkunion.union2024.board.domain.ClubBoard;
import skkunion.union2024.club.common.domain.ClubMember;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "board_like",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = { "club_board_id", "club_member_id" })
        }
)
public class BoardLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "club_board_id")
    private ClubBoard clubBoard;

    @Column(nullable = false)
    private Long clubMemberId;

    public BoardLike(ClubBoard clubBoard, Long clubMemberId) {
        this.clubBoard = clubBoard;
        this.clubMemberId = clubMemberId;
    }

    public static BoardLike of(ClubBoard clubBoard, Long clubMemberId) {
        return new BoardLike(clubBoard, clubMemberId);
    }
}
