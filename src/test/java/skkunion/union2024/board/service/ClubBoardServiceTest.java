package skkunion.union2024.board.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import skkunion.union2024.board.domain.ClubBoard;
import skkunion.union2024.board.domain.repository.ClubBoardRepository;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubBoardServiceTest {

    @Autowired
    private ClubBoardRepository clubBoardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private EntityManager em;

    @Test
    void 클럽_게시글_작성() {
        Club club = clubRepository.findClubBySlug("algoGood").get();
        Member member = memberRepository.findByEmail("skkunion2024@gmail.com").get();
        ClubMember clubMember = clubMemberRepository.findByClubAndMember(club, member).get();

        for (int i = 1; i < 1000; i++) {
            for (int j = 1; j <= 10000; j++) {
                String title = "테스트" + i + j;
                String content = "테스트 내용" + i + j;
                clubBoardRepository.save(ClubBoard.of(title, content, club, clubMember, "skkunion2024@gmail.com", clubMember.getNickName()));
            }
            em.flush();
        }


    }

}