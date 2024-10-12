package skkunion.union2024.like.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import skkunion.union2024.board.domain.ClubBoard;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = LAZY)
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
