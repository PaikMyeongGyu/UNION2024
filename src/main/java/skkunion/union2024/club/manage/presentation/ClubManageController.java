package skkunion.union2024.club.manage.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import skkunion.union2024.club.manage.presentation.dto.CreateClubRequest;
import skkunion.union2024.club.manage.service.ClubManageService;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class ClubManageController {

    private final ClubManageService clubManageService;
    private final MemberService memberService;

    @PostMapping("/clubs/manage")
    public ResponseEntity<Void> createClub(
            @RequestBody CreateClubRequest req,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        Member findMember = memberService.findMemberBy(userEmail).get();
        clubManageService.createClub(req.covertToCreateClubDto(findMember));
        return ResponseEntity.status(CREATED).build();
    }
}
