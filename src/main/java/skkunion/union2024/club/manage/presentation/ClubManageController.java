package skkunion.union2024.club.manage.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.service.ClubGeneralService;
import skkunion.union2024.club.dto.request.CreateClubRequest;
import skkunion.union2024.club.dto.response.ClubMemberDto;
import skkunion.union2024.club.dto.response.ClubMemberResponse;
import skkunion.union2024.club.manage.service.ClubManageService;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class ClubManageController {

    private final int PAGE_SIZE = 15;
    private final ClubManageService clubManageService;
    private final ClubGeneralService clubGeneralService;
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

    @GetMapping("/clubs/members")
    public ResponseEntity<ClubMemberResponse> showMembersInClub(
        @RequestParam String clubSlug,
        @RequestParam(required = false) Long clubMemberId,
        Authentication authentication
    ) {
        Club findClub = clubGeneralService.findClubBy(clubSlug);

        String userEmail = authentication.getName();
        Member findMember = memberService.findMemberBy(userEmail).get();
        clubGeneralService.findClubMemberBy(findClub, findMember);

        if (clubMemberId == null) {
            List<ClubMember> findClubMembersWithSlug = clubGeneralService.findMemberBySlug(clubSlug);
            return generateResponse(findClubMembersWithSlug, findClub);
        }

        List<ClubMember> findClubMembersWithSlugAndId = clubGeneralService.findMemberBySlugAndId(clubSlug, clubMemberId);
        return generateResponse(findClubMembersWithSlugAndId, findClub);
    }

    private ResponseEntity<ClubMemberResponse> generateResponse(List<ClubMember> findClubMember, Club findClub) {
        ClubMemberResponse clubMemberResponse = generateClubMemberResponse(findClubMember, findClub);
        return ResponseEntity.ok(clubMemberResponse);
    }

    private ClubMemberResponse generateClubMemberResponse(List<ClubMember> findClubMembersWithSlug, Club findClub) {
        Boolean hasNext = findClubMembersWithSlug.size() > PAGE_SIZE ? TRUE : FALSE;
        Long nextId = hasNext ? findClubMembersWithSlug.get(findClubMembersWithSlug.size() - 2).getId() : null;
        Collections.sort(findClubMembersWithSlug); // President, Manager, General 순
        findClubMembersWithSlug.remove(findClubMembersWithSlug.size() - 1);


        Map<ClubAuthority, List<ClubMemberDto>> clubMembers
                                    = findClubMembersWithSlug.stream()
                                                     .map(ClubMemberDto::convertToClubMemberDto)
                                                     .collect(groupingBy(ClubMemberDto::authority));

        var response = new ClubMemberResponse(findClub.getClubName(),
                                              findClub.getTotalMembers(),
                                              hasNext,
                                              nextId,
                                              clubMembers);
        return response;
    }

}
