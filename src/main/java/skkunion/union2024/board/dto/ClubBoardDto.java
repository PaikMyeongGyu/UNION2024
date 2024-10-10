package skkunion.union2024.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ClubBoardDto{
    Long id;
    String title;
    String description;
    String nickname;
    LocalDateTime joinedAt;
}
