package skkunion.union2024.club.common.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import skkunion.union2024.club.common.service.ClubGeneralService;
import skkunion.union2024.club.dto.request.ClubJoinRequest;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
public class ClubGeneralController {
    private final ClubGeneralService clubGeneralService;
    private final MemberService memberService;

    @PostMapping("/clubs/{clubSlug}")
    public ResponseEntity<Void> joinClub(
            @RequestBody ClubJoinRequest req,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        Member findMember = memberService.findMemberBy(userEmail).get();

        clubGeneralService.joinClub(req.convertToClubJoinDto(findMember));
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
