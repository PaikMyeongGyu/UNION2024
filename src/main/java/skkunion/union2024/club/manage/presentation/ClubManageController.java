package skkunion.union2024.club.manage.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.service.ClubGeneralService;
import skkunion.union2024.club.dto.request.CreateClubRequest;
import skkunion.union2024.club.dto.response.ClubMemberDto;
import skkunion.union2024.club.dto.response.ClubMemberSelectDto;
import skkunion.union2024.club.dto.response.ClubMemberResponse;
import skkunion.union2024.club.manage.service.ClubManageService;
import skkunion.union2024.global.annotation.AuthMember;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.dto.AuthMemberDto;
import skkunion.union2024.member.service.MemberService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.http.HttpStatus.CREATED;

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
        @RequestParam String clubSlug,
        @RequestParam(required = false) Long clubMemberId,
        @AuthMember AuthMemberDto authMember
    ) {
        if (clubMemberId == null) {
            return ResponseEntity.ok(clubGeneralService.findMemberBySlug(clubSlug, authMember.memberId()));
        }
        return ResponseEntity.ok(clubGeneralService.findMemberBySlugAndId(clubSlug, authMember.memberId(), clubMemberId));
    }

}
