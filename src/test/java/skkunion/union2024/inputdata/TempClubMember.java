package skkunion.union2024.inputdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.club.manage.service.ClubManageService;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;
import skkunion.union2024.club.manage.service.servicedto.CreateClubDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TempClubMember {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ClubMemberRepository clubMemberRepository;

    @Autowired
    ClubManageService clubManageService;

    @Test
    void 더미데이터_삽입_100000() {
        for (int i = 101; i <= 100000; i++) {
            Member member = new Member("tester" + i, "tester" + i + "@example.com", "123456789");
            memberRepository.save(member);
        }
    }

    @Test
    void 더미데이터_삽입_100000_이후() {
        for (int i = 260001; i <= 270000; i++) {
            Member member = new Member("tester" + i, "tester" + i + "@example.com", "123456789");
            memberRepository.save(member);
        }
    }


//    @Test
//    void 더미클럽_가입_100000() {
//        for (int i = 50001; i <= 100000; i++) {
//            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
//            Club findClub = clubRepository.findClubBySlug("algoGood").get();
//
//            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
//            clubMemberRepository.save(general);
//        }
//    }
//
//    @Test
//    void 더미클럽_생성_100() {
//        Member findMember = memberRepository.findByEmail("test1@gmail.com").get();
//        for (int i = 1; i <= 100; i++) {
//            clubManageService.createClub(new CreateClubDto("club" + i, "club" + i, "레프리", "no", findMember));
//        }
//    }

    private int index = 30;
    @Test
    void 더미클럽_가입_00000() {
        for (int i = 1; i <= 3000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_10000() {
        for (int i = 1001; i <= 2000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_20000() {
        for (int i = 2001; i <= 3000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_30000() {
        for (int i = 3001; i <= 4000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_40000() {
        for (int i = 4001; i <= 5000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_50000() {
        for (int i = 5001; i <= 6000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_60000() {
        for (int i = 6001; i <= 7000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_70000() {
        for (int i = 7001; i <= 8000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_80000() {
        for (int i = 8001; i <= 9000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }

    @Test
    void 더미클럽_가입_90000() {
        for (int i = 9001; i <= 10000; i++) {
            Member findMember = memberRepository.findByEmail("tester" + i + "@example.com").get();
            Club findClub = clubRepository.findClubBySlug("club" + index).get();
            ClubMember general = ClubMember.General(findClub, findMember, "tester" + i);
            clubMemberRepository.save(general);
        }
    }
}
