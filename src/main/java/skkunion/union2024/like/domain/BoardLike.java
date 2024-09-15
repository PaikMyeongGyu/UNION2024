package skkunion.union2024.like.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "board_like", indexes = {
        @Index(name = "idx_board_like", columnList = "board_id, member_email")
})
public class BoardLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "board_like_id")
    private Long id;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false, length = 40)
    private String memberEmail;

}
