package skkunion.union2024.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.board.domain.ClubBoard;
import skkunion.union2024.board.domain.repository.ClubBoardRepository;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.global.exception.ClubBoardException;
import skkunion.union2024.global.exception.exceptioncode.ExceptionCode;
import skkunion.union2024.like.domain.BoardLike;
import skkunion.union2024.like.domain.repository.LikeRepository;
import skkunion.union2024.lock.repository.LockRepository;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import java.util.Optional;

import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ClubBoardService {

    private final ClubMemberRepository clubMemberRepository;
    private final ClubBoardRepository clubBoardRepository;
    private final LikeRepository likeRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final LockRepository lockRepository;

    @Transactional
    public void registerClubBoard(String slug, String email,
                                  String title, String content) {
        Club findClub = clubRepository.findClubBySlug(slug)
                .orElseThrow(() -> new ClubBoardException(CLUB_NOT_FOUND));
        ClubMember findClubMember = getClubMemberWithValidation(slug, email);

        var clubBoard = ClubBoard.of(title, content, findClub, findClubMember, email, findClubMember.getNickName());
        clubBoardRepository.save(clubBoard);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void likeClubBoard(Long boardId, String slug, String email) {
        ClubMember clubMember = getClubMemberWithValidation(slug, email);
        ClubBoard findClubBoard = clubBoardRepository.findById(boardId)
                .orElseThrow(() -> new ClubBoardException(CLUB_BOARD_NOT_FOUND));

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

    @Transactional
    public void namedLockLikeClubBoard(Long boardId, String slug, String email) {
        try {
            lockRepository.getLock(boardId + slug + email);
            likeClubBoard(boardId, slug, email);
        } finally {
            lockRepository.releaseLock(boardId + slug + email);
        }
    }

    private ClubMember getClubMemberWithValidation(String slug, String email) {
        Club findClub = clubRepository.findClubBySlug(slug)
                .orElseThrow(() -> new ClubBoardException(CLUB_NOT_FOUND));
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ClubBoardException(ACCOUNT_NOT_FOUND));
        ClubMember findClubMember = clubMemberRepository.findByClubAndMember(findClub, findMember)
                .orElseThrow(() -> new ClubBoardException(CLUB_MEMBER_NOT_FOUND));

        return findClubMember;
    }
}
