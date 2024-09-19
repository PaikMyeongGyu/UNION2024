package skkunion.union2024.board.presentation.dto.request;

public record RegisterBoardRequest(
        String clubSlug,
        String title,
        String content
) {
}
