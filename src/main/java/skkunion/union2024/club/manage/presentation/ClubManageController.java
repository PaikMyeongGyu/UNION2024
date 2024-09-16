package skkunion.union2024.club.manage.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import skkunion.union2024.club.manage.presentation.dto.request.ClubJoinRequest;
import skkunion.union2024.club.manage.presentation.dto.request.CreateClubRequest;
import skkunion.union2024.club.manage.service.ClubManageService;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
public class ClubManageController {

    private final ClubManageService clubManageService;
    private final MemberService memberService;

    @PostMapping("/clubs/create")
    public ResponseEntity<Void> createClub(
            @RequestBody CreateClubRequest req,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        Member findMember = memberService.findMemberBy(userEmail).get();

        clubManageService.createClub(req.covertToCreateClubDto(findMember));
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/clubs/{clubSlug}")
    public ResponseEntity<Void> joinClub(
            @RequestBody ClubJoinRequest req,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        Member findMember = memberService.findMemberBy(userEmail).get();

        clubManageService.joinClub(req.convertToClubJoinDto(findMember));
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
