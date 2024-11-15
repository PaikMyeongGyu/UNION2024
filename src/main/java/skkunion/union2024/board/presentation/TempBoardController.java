package skkunion.union2024.board.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.board.dto.response.ClubBoardResponse;
import skkunion.union2024.board.presentation.dto.request.RegisterBoardRequest;
import skkunion.union2024.board.service.ClubBoardService;
import skkunion.union2024.board.service.TempClubBoardService;
import skkunion.union2024.global.annotation.AuthMember;
import skkunion.union2024.member.dto.AuthMemberDto;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class TempBoardController {

    private final TempClubBoardService clubBoardService;

    @GetMapping("/tboards")
    public ResponseEntity<ClubBoardResponse> getClubBoard(
            @RequestParam("clubSlug") String clubSlug,
            @RequestParam(value = "page", required = false) Long page,
            @AuthMember AuthMemberDto authMemberDto) {

        if (page == null) {
            page = 0L;
        } else {
            page = page * 15;
        }
        ClubBoardResponse clubBoards
                = clubBoardService.getClubBoardWithOffset(clubSlug, authMemberDto.memberId(), page);
        return ResponseEntity.status(OK).body(clubBoards);
    }
}
