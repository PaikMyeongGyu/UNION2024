package skkunion.union2024.club.common.service;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static org.springframework.data.repository.util.ClassUtils.ifPresent;
import static skkunion.union2024.club.dto.response.ClubMemberDto.COMP_AUTH_JOINED_AT_DESC;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.auth.domain.Authority;
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

import lombok.RequiredArgsConstructor;

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
        Club findClub = getClubBy(req);
        isDuplicateJoining(req, findClub);
        isDuplicateNickName(req, findClub);

        clubRepository.updateTotalMembersBySlug(req.clubSlug());

        ClubMember generalMember = ClubMember.General(findClub, req.member(), req.nickName());
        clubMemberRepository.save(generalMember);
    }

    @Transactional(readOnly = true)
    public ClubMemberResponse findMemberBySlug(String slug, Long memberId) {
        Club findClub = IsMemberInClub(slug, memberId);
        return generateClubMemberResponse(clubQueryRepository.getClubMembers(slug), findClub);
    }


    @Transactional(readOnly = true)
    public ClubMemberResponse findMemberBySlugAndAuthorityAndId(
            Long memberId,
            String slug,
            ClubAuthority authority,
            Long clubMemberId) {

        Club findClub = IsMemberInClub(slug, memberId);
        List<ClubMemberDto> clubMembers = clubQueryRepository.getClubMembers(slug, authority, clubMemberId);
        return generateClubMemberResponse(clubMembers, findClub);
    }

    private Club IsMemberInClub(String slug, Long memberId) {
        Club findClub = clubRepository.findClubBySlug(slug)
                .orElseThrow(() -> new ClubException(CLUB_NOT_FOUND));

        Member findMember = memberService.findMemberById(memberId).get();

        clubMemberRepository.findByClubAndMember(findClub, findMember)
                .orElseThrow(() -> new ClubException(CLUB_MEMBER_NOT_FOUND));
        return findClub;
    }

    private ClubMemberResponse generateClubMemberResponse(List<ClubMemberDto> findClubMembersWithSlug, Club findClub) {
        Boolean hasNext = findClubMembersWithSlug.size() > PAGE_SIZE ? TRUE : FALSE;
        ClubAuthority authority = hasNext ? findClubMembersWithSlug.get(findClubMembersWithSlug.size() - 2).authority() : null;
        Long nextId = hasNext ? findClubMembersWithSlug.get(findClubMembersWithSlug.size() - 2).id() : null;
        var result = findClubMembersWithSlug.stream().limit(PAGE_SIZE).toList();

        Map<ClubAuthority, List<ClubMemberSelectDto>> clubMembers
                = result.stream()
                                         .map(ClubMemberDto::convertToClubMemberDto)
                                         .collect(groupingBy(ClubMemberSelectDto::authority));

        return new ClubMemberResponse(findClub.getClubName(),
                                      authority,
                                      nextId,
                                      findClub.getTotalMembers(),
                                      hasNext,
                                      clubMembers);
    }

    private void isDuplicateNickName(ClubJoinDto req, Club findClub) {
        clubMemberRepository.findByClubAndNickName(findClub, req.nickName())
                .ifPresent(club -> { throw new ClubException(CLUB_MEMBER_DUPLICATED_NICKNAME); });
    }

    private void isDuplicateJoining(ClubJoinDto req, Club findClub) {
        clubMemberRepository.findByClubAndMember(findClub, req.member())
                .ifPresent(club -> { throw new ClubException(CLUB_MEMBER_ALREADY_EXIST); });
    }

    private Club getClubBy(ClubJoinDto req) {
        return clubRepository.findClubBySlug(req.clubSlug())
                .orElseThrow(() -> new ClubException(CLUB_NOT_FOUND));
    }

}
