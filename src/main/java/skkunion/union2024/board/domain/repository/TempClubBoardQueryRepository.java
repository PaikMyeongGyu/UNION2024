package skkunion.union2024.board.domain.repository;

import static skkunion.union2024.board.domain.QClubBoard.clubBoard;
import static skkunion.union2024.global.util.PageParameterUtils.PAGE_SIZE;

import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Repository;

import skkunion.union2024.board.dto.ClubBoardDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TempClubBoardQueryRepository {

    private final JPAQueryFactory queryFactory;
    // index : club_board_club_id_club_board_id(club_id, club_board_id-1)
    public List<ClubBoardDto> offsetPaging(Long offset, String slug) {

        if (offset == 0L) {
            return queryFactory
                    .select(Projections.fields(ClubBoardDto.class,
                            clubBoard.id,
                            clubBoard.title,
                            clubBoard.content,
                            clubBoard.nickname,
                            clubBoard.joinedAt))
                    .from(clubBoard)
                    .where(clubBoard.club.slug.eq(slug))
                    .orderBy(clubBoard.club.slug.asc(), clubBoard.id.desc())
                    .limit(PAGE_SIZE + 1)
                    .fetch();
        }

        return queryFactory
                .select(Projections.fields(ClubBoardDto.class,
                        clubBoard.id,
                        clubBoard.title,
                        clubBoard.content,
                        clubBoard.nickname,
                        clubBoard.joinedAt))
                .from(clubBoard)
                .where(clubBoard.club.slug.eq(slug))
                .orderBy(clubBoard.club.slug.asc(), clubBoard.id.desc())
                .offset(offset)
                .limit(PAGE_SIZE + 1)
                .fetch();
    }
}
