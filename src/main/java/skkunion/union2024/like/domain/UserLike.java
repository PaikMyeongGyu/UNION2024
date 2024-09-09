package skkunion.union2024.like.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "user_like", indexes = {
        @Index(name = "idx_like", columnList = "board_id, member_id")
})
public class UserLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_like_id")
    private Long id;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long memberId;

}
