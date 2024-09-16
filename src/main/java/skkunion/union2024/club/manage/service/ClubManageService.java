package skkunion.union2024.club.manage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.manage.service.request.CreateClubDto;
import skkunion.union2024.club.manage.service.request.ClubJoinDto;
import skkunion.union2024.global.exception.ClubException;

import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ClubManageService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public void createClub(CreateClubDto req) {
        clubRepository.findClubByClubName(req.clubName())
                      .ifPresent(club -> { throw new ClubException(CLUB_NAME_ALREADY_EXIST); });
        clubRepository.findClubBySlug(req.clubSlug())
                      .ifPresent(club -> { throw new ClubException(CLUB_NAME_ALREADY_EXIST); });

        Club createdClub = Club.of(req.clubSlug(), req.clubName(), req.presidentName(), req.description());
        clubRepository.save(createdClub);

        ClubMember president = ClubMember.President(createdClub, req.member(), req.presidentName());
        clubMemberRepository.save(president);
    }

    @Transactional
    public void joinClub(ClubJoinDto req) {
        Club findClub = clubRepository.findClubBySlug(req.clubSlug())
                                      .orElseThrow(() -> new ClubException(CLUB_NOT_FOUND));
        clubMemberRepository.findByClubAndMember(findClub,req.member())
                            .ifPresent(club -> { throw new ClubException(CLUB_MEMBER_ALREADY_EXIST); });
        clubMemberRepository.findByNickname(req.nickName())
                            .ifPresent(club -> { throw new ClubException(CLUB_MEMBER_DUPLICATED_NICKNAME); });

        clubRepository.updateTotalMembersBySlug(req.clubSlug());

        ClubMember generalMember = ClubMember.General(findClub, req.member(), req.nickName());
        clubMemberRepository.save(generalMember);
    }
}
