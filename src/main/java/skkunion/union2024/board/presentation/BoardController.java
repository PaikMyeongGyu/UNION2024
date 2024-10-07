package skkunion.union2024.board.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.board.dto.response.ClubBoardResponse;
import skkunion.union2024.board.presentation.dto.request.RegisterBoardRequest;
import skkunion.union2024.board.service.ClubBoardService;
import skkunion.union2024.global.annotation.AuthMember;
import skkunion.union2024.member.dto.AuthMemberDto;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequiredArgsConstructor
public class BoardController {

    private final ClubBoardService clubBoardService;

    @PostMapping("/boards")
    public ResponseEntity<Void> registerBoard(
            @RequestBody RegisterBoardRequest req,
            @AuthMember AuthMemberDto authMemberDto) {

        clubBoardService.registerClubBoard(req.clubSlug(), authMemberDto.memberId(), req.title(), req.content());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/boards/like")
    public ResponseEntity<Void> likeAction(
            @RequestParam("boardId") Long boardId,
            @RequestParam("clubSlug") String clubSlug,
            @AuthMember AuthMemberDto authMemberDto) {

        clubBoardService.likeClubBoard(boardId, clubSlug, authMemberDto.memberId());
        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/boards")
    public ResponseEntity<ClubBoardResponse> getClubBoard(
            @RequestParam("clubSlug") String clubSlug,
            @RequestParam(value = "boardId", required = false) Long boardId,
            @AuthMember AuthMemberDto authMemberDto) {

        ClubBoardResponse clubBoards
                = clubBoardService.getClubBoardWithNoOffset(clubSlug, authMemberDto.memberId(), boardId);
        return ResponseEntity.status(OK).body(clubBoards);
    }
}
