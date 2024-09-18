package skkunion.union2024.club.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubQueryRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.club.dto.response.ClubMemberDto;
import skkunion.union2024.club.dto.response.ClubMemberResponse;
import skkunion.union2024.club.dto.response.ClubMemberSelectDto;
import skkunion.union2024.club.manage.service.servicedto.ClubJoinDto;
import skkunion.union2024.global.exception.ClubException;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ClubGeneralService {

    private final int PAGE_SIZE = 15;

    private final MemberService memberService;

    private final ClubRepository clubRepository;
    private final ClubQueryRepository clubQueryRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public void joinClub(ClubJoinDto req) {
        Club findClub = clubRepository.findClubBySlug(req.clubSlug())
                .orElseThrow(() -> new ClubException(CLUB_NOT_FOUND));
        clubMemberRepository.findByClubAndMember(findClub,req.member())
                .ifPresent(club -> { throw new ClubException(CLUB_MEMBER_ALREADY_EXIST); });
        clubMemberRepository.findByNickName(req.nickName())
                .ifPresent(club -> { throw new ClubException(CLUB_MEMBER_DUPLICATED_NICKNAME); });

        clubRepository.updateTotalMembersBySlug(req.clubSlug());

        ClubMember generalMember = ClubMember.General(findClub, req.member(), req.nickName());
        clubMemberRepository.save(generalMember);
    }

    @Transactional(readOnly = true)
    public ClubMemberResponse findMemberBySlug(String slug, String email) {
        Club findClub = IsMemberInClub(slug, email);

        return generateClubMemberResponse(clubQueryRepository.getMembersWithoutId(slug), findClub);
    }


    @Transactional(readOnly = true)
    public ClubMemberResponse findMemberBySlugAndId(String slug, String email, Long clubMemberId) {
        Club findClub = IsMemberInClub(slug, email);

        return generateClubMemberResponse(clubQueryRepository.getMembersWithId(slug, clubMemberId), findClub);
    }

    private Club IsMemberInClub(String slug, String email) {
        Club findClub = clubRepository.findClubBySlug(slug)
                .orElseThrow(() -> new ClubException(CLUB_NOT_FOUND));

        Member findMember = memberService.findMemberBy(email).get();

        clubMemberRepository.findByClubAndMember(findClub, findMember)
                .orElseThrow(() -> new ClubException(CLUB_MEMBER_NOT_FOUND));
        return findClub;
    }

    private ClubMemberResponse generateClubMemberResponse(List<ClubMemberDto> findClubMembersWithSlug, Club findClub) {
        Boolean hasNext = findClubMembersWithSlug.size() > PAGE_SIZE ? TRUE : FALSE;
        Long nextId = hasNext ? findClubMembersWithSlug.get(findClubMembersWithSlug.size() - 2).id() : null;

        Collections.sort(findClubMembersWithSlug, comparing(ClubMemberDto::authority).
                                                  thenComparing(ClubMemberDto::id)); // President, Manager, General 순
        findClubMembersWithSlug.remove(findClubMembersWithSlug.size() - 1);

        Map<ClubAuthority, List<ClubMemberSelectDto>> clubMembers
                = findClubMembersWithSlug.stream()
                                         .map(ClubMemberDto::convertToClubMemberDto)
                                         .collect(groupingBy(ClubMemberSelectDto::authority));

        var response = new ClubMemberResponse(findClub.getClubName(),
                                              findClub.getTotalMembers(),
                                              hasNext,
                                              nextId,
                                              clubMembers);
        return response;
    }
}
