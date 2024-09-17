package skkunion.union2024.club.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubQueryRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.club.dto.response.ClubMemberDto;
import skkunion.union2024.club.manage.service.servicedto.ClubJoinDto;
import skkunion.union2024.global.exception.ClubException;
import skkunion.union2024.member.domain.Member;

import java.util.List;

import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ClubGeneralService {

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
    public ClubMember findClubMemberBy(Club club, Member member) {
        return clubMemberRepository.findByClubAndMember(club, member)
                .orElseThrow(() -> new ClubException(CLUB_MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Club findClubBy(String slug) {
        return clubRepository.findClubBySlug(slug)
                .orElseThrow(() -> new ClubException(CLUB_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ClubMember> findMemberBySlug(String slug) {
        return clubQueryRepository.getMembersWithoutId(slug);
    }

    @Transactional(readOnly = true)
    public List<ClubMember> findMemberBySlugAndId(String slug, Long clubMemberId) {
        return clubQueryRepository.getMembersWithId(slug, clubMemberId);
    }
}
