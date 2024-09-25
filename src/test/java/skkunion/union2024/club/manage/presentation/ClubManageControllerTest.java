package skkunion.union2024.club.manage.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import skkunion.union2024.club.common.domain.repository.ClubQueryRepository;

@SpringBootTest
class ClubManageControllerTest {

    @Autowired
    ClubManageController clubManageController;

    @Autowired
    ClubQueryRepository clubQueryRepository;

//    @Test
//    void 쿼리확인() {
//        clubQueryRepository.getMembersWithoutId("algoGood");
//    }
}