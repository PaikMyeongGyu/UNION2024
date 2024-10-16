package skkunion.union2024.board.service;

import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;
import static skkunion.union2024.global.util.PageParameterUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.board.domain.ClubBoard;
import skkunion.union2024.board.domain.repository.ClubBoardQueryRepository;
import skkunion.union2024.board.domain.repository.ClubBoardRepository;
import skkunion.union2024.board.dto.ClubBoardDto;
import skkunion.union2024.board.dto.response.ClubBoardResponse;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.global.exception.ClubBoardException;
import skkunion.union2024.like.domain.BoardLike;
import skkunion.union2024.like.domain.repository.LikeRepository;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubBoardService {
    private final ClubMemberRepository clubMemberRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final LikeRepository likeRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubBoardQueryRepository clubBoardQueryRepository;

    @Transactional
    public void registerClubBoard(String slug, Long memberId, String title, String content) {
        Club findClub = getClubBySlug(slug);
        Member findMember = getMemberById(memberId);
        ClubMember findClubMember = getClubMemberWithClubAndMember(findClub, findMember);

        var clubBoard = ClubBoard.builder()
                                 .title(title)
                                 .content(content)
                                 .club(findClub)
                                 .clubMember(findClubMember)
                                 .memberEmail(findMember.getEmail())
                                 .nickname(findClubMember.getNickName()).build();
        clubBoardRepository.save(clubBoard);
    }

    @Transactional
    public void likeClubBoard(Long boardId, String slug, Long memberId) {
        ClubMember clubMember = getClubMemberWithValidation(slug, memberId);
        ClubBoard findClubBoard = getClubBoardById(boardId);

        Optional<BoardLike> findLike =
                likeRepository.findLikeByClubBoardAndClubMemberId(findClubBoard, clubMember.getId());

        if (findLike.isPresent()) {
            likeRepository.delete(findLike.get());
            clubBoardRepository.decreaseLikes(findClubBoard.getId());
        } else {
            likeRepository.save(BoardLike.of(findClubBoard, clubMember.getId()));
            clubBoardRepository.increaseLikes(findClubBoard.getId());
        }
    }

    @Transactional(readOnly = true)
    public ClubBoardResponse getClubBoardWithNoOffset(String slug, Long memberId, Long boardCursorId) {
        getClubMemberWithValidation(slug, memberId);

        List<ClubBoardDto> clubBoardDtos = clubBoardQueryRepository.noOffsetPaging(boardCursorId, slug);
        boolean hasNext = hasNext(clubBoardDtos);
        int size = getSize(clubBoardDtos);

        Long nextCursorId = null;
        if (hasNext) {
            clubBoardDtos.remove(size);
            nextCursorId = getLongCursor(clubBoardDtos, ClubBoardDto::getId);
        }
        return new ClubBoardResponse(slug, size, hasNext, nextCursorId, clubBoardDtos);
    }

    private ClubMember getClubMemberWithClubAndMember(Club club, Member member) {
        return clubMemberRepository.findByClubAndMember(club, member)
                                   .orElseThrow(() -> new ClubBoardException(CLUB_MEMBER_NOT_FOUND));
    }

    private ClubMember getClubMemberWithValidation(String slug, Long memberId) {
        Club club = getClubBySlug(slug);
        Member member = getMemberById(memberId);
        return clubMemberRepository.findByClubAndMember(club, member)
                                   .orElseThrow(() -> new ClubBoardException(CLUB_MEMBER_NOT_FOUND));
    }

    private Club getClubBySlug(String slug) {
        return clubRepository.findClubBySlug(slug)
                             .orElseThrow(() -> new ClubBoardException(CLUB_NOT_FOUND));
    }

    private ClubBoard getClubBoardById(Long boardId) {
        return clubBoardRepository.findById(boardId)
                                  .orElseThrow(() -> new ClubBoardException(CLUB_BOARD_NOT_FOUND));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                               .orElseThrow(() -> new ClubBoardException(ACCOUNT_NOT_FOUND));
    }

}
