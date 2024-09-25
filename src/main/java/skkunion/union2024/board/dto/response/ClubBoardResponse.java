package skkunion.union2024.board.dto.response;

import skkunion.union2024.board.dto.ClubBoardDto;

import java.util.List;

public record ClubBoardResponse(
        String slug,
        Integer size,
        Boolean hasNext,
        Long boardCursorId,
        List<ClubBoardDto> clubBoards
) {
}
