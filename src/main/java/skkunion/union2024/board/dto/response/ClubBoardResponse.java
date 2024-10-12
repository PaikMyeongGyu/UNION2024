package skkunion.union2024.board.dto.response;

import java.util.List;

import skkunion.union2024.board.dto.ClubBoardDto;

public record ClubBoardResponse(
        String slug,
        Integer size,
        Boolean hasNext,
        Long boardCursorId,
        List<ClubBoardDto> clubBoards
) {
}
