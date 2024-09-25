package skkunion.union2024.board.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import skkunion.union2024.board.dto.ClubBoardDto;

import java.util.List;

import static skkunion.union2024.board.domain.QClubBoard.clubBoard;

@Repository
@RequiredArgsConstructor
public class ClubBoardQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private final int PAGE_SIZE = 16;

    public List<ClubBoardDto> noOffsetPaging(Long clubBoardId, String slug) {

        BooleanBuilder dynamicLtId = new BooleanBuilder();

        if (clubBoardId != null) {
            dynamicLtId.and(clubBoard.id.lt(clubBoardId));
        }

        return queryFactory
                .select(Projections.fields(ClubBoardDto.class,
                        clubBoard.id,
                        clubBoard.title,
                        clubBoard.content,
                        clubBoard.nickname,
                        clubBoard.joinedAt))
                .from(clubBoard)
                .where(dynamicLtId
                        .and(clubBoard.club.slug.eq(slug)))
                .orderBy(clubBoard.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }
}
