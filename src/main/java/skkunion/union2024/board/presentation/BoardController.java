package skkunion.union2024.board.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.board.dto.response.ClubBoardResponse;
import skkunion.union2024.board.presentation.dto.request.RegisterBoardRequest;
import skkunion.union2024.board.service.ClubBoardService;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequiredArgsConstructor
public class BoardController {

    private final ClubBoardService clubBoardService;

    @PostMapping("/boards")
    public ResponseEntity<Void> registerBoard(
            @RequestBody RegisterBoardRequest req,
            Authentication authentication) {

        String email = authentication.getName();
        clubBoardService.registerClubBoard(req.clubSlug(), email, req.title(), req.content());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/boards/like")
    public ResponseEntity<Void> likeAction(
            @RequestParam("boardId") Long boardId,
            @RequestParam("clubSlug") String clubSlug,
            Authentication authentication) {

        String email = authentication.getName();
        clubBoardService.likeClubBoard(boardId, clubSlug, email);

        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/boards")
    public ResponseEntity<ClubBoardResponse> getClubBoard(
            @RequestParam("clubSlug") String clubSlug,
            @RequestParam(value = "boardId", required = false) Long boardId,
            Authentication authentication) {

        String email = authentication.getName();
        ClubBoardResponse clubBoards = clubBoardService.getClubBoardWithNoOffset(clubSlug, email, boardId);
        return ResponseEntity.status(OK).body(clubBoards);
    }
}
