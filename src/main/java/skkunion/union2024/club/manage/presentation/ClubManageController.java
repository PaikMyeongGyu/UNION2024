package skkunion.union2024.club.manage.presentation;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.service.ClubGeneralService;
import skkunion.union2024.club.dto.request.CreateClubRequest;
import skkunion.union2024.club.dto.response.ClubMemberResponse;
import skkunion.union2024.club.manage.service.ClubManageService;
import skkunion.union2024.global.annotation.AuthMember;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.dto.AuthMemberDto;
import skkunion.union2024.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ClubManageController {

    private final ClubManageService clubManageService;
    private final ClubGeneralService clubGeneralService;
    private final MemberService memberService;

    @PostMapping("/clubs/create")
    public ResponseEntity<Void> createClub(
            @RequestBody CreateClubRequest req,
            @AuthMember AuthMemberDto authMember
    ) {
        Member findMember = memberService.findMemberById(authMember.memberId()).get();

        clubManageService.createClub(req.covertToCreateClubDto(findMember));
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/clubs/members")
    public ResponseEntity<ClubMemberResponse> showMembersInClub(
        @RequestParam String slug,
        @RequestParam(required = false) ClubAuthority authority,
        @RequestParam(required = false) Long nextId,
        @AuthMember AuthMemberDto authMember
    ) {
        if (nextId == null) {
            return ResponseEntity.ok(clubGeneralService.findMemberBySlug(slug, authMember.memberId()));
        }

        return ResponseEntity
                .ok(clubGeneralService.findMemberBySlugAndAuthorityAndId(
                        authMember.memberId(),
                        slug,
                        authority,
                        nextId));
    }

}
