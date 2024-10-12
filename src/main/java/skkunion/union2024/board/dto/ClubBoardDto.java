package skkunion.union2024.board.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubBoardDto{
    Long id;
    String title;
    String description;
    String nickname;
    LocalDateTime joinedAt;
}
