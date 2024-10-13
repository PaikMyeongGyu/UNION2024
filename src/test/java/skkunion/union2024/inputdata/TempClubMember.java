package skkunion.union2024.inputdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import skkunion.union2024.board.domain.ClubBoard;
import skkunion.union2024.board.domain.repository.ClubBoardRepository;
import skkunion.union2024.board.service.ClubBoardService;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.club.manage.service.ClubManageService;
import skkunion.union2024.like.domain.BoardLike;
import skkunion.union2024.like.domain.repository.LikeRepository;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;
import skkunion.union2024.club.manage.service.servicedto.CreateClubDto;
import skkunion.union2024.member.service.MemberService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TempClubMember {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ClubMemberRepository clubMemberRepository;

    @Autowired
    ClubManageService clubManageService;

    @Autowired
    ClubBoardService clubBoardService;

    @Autowired
    ClubBoardRepository clubBoardRepository;

    @Autowired
    LikeRepository likeRepository;

    @Test
    void 더미유저_삽입_1000() {
        for (int i = 180001; i <= 210000; i++) {
            Member member = new Member("tester" + i, "tester" + i + "@example.com", "123456789");
            memberRepository.save(member);
            memberService.activateMemberByEmail("tester" + i + "@example.com");
        }
    }

    @Test
    void 더미클럽_생성_10() {
        for (int i = 1; i <= 10; i++) {
            Club club = Club.of("algoGood" + i, "알고리즘" + i, "레프리" + i, "알고리즘을 위한 동아리입니다.");
            clubRepository.save(club);

            Member president = memberRepository.findById(6L).get();
            ClubMember clubPresident = ClubMember.President(club, president, "레프리" + i);
            clubMemberRepository.save(clubPresident);
        }
    }

    @Test
    void 더미게시글_생성_50000() {
        for (int i = 1270001; i <= 1300000; i++) {
            clubBoardService.registerClubBoard("algoGood" + (i % 10 + 1),
                                          30L,
                                               "테스트용 게시글입니다." + i,
                                            "테스트용 게시글입니다." + i);
        }
    }

    @Test
    void 더미클럽_10_가입_1000() {
        for (int i = 1; i <= 1000; i++) {
            Member member = memberRepository.findByEmail("tester" + i + "@example.com").get();
            for (int j = 1; j <= 10; j++) {
                Club club = clubRepository.findClubBySlug("algoGood" + j).get();
                ClubMember general = ClubMember.General(club, member, "tester" + i);
                clubMemberRepository.save(general);
            }
        }
    }

    @Test
    void 더미게시글_좋아요_10_1000() {
        for (long i = 1L; i <= 50L; i++) {
            ClubBoard clubBoard = clubBoardRepository.findById(i).get();
            for (int j = 1; j <= 1000; j++) {
                Club club = clubBoard.getClub();
                ClubMember clubMember = clubMemberRepository.findByClubAndNickName(club,"tester" + j).get();
                likeRepository.save(BoardLike.of(clubBoard, clubMember.getId()));
                clubBoardRepository.increaseLikes(clubBoard.getId());
            }
        }
    }
}
