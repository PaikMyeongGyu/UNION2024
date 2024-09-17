package skkunion.union2024.club.common.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.QClub;
import skkunion.union2024.club.dto.response.ClubMemberDto;

import java.util.List;

import static skkunion.union2024.club.common.domain.ClubAuthority.*;
import static skkunion.union2024.club.common.domain.QClubMember.clubMember;

@Repository
@RequiredArgsConstructor
public class ClubQueryRepository {

    private final int PAGE_SIZE = 16;
    private final JPAQueryFactory queryFactory;

    public List<ClubMember> getMembersWithoutId(String slug) {
        return queryFactory
                .select(clubMember)
                .from(clubMember)
                .where(clubMember.club.slug.eq(slug)
                        .and(clubMember.clubAuthority.in(PRESIDENT, MANAGER, GENERAL)))
                .limit(PAGE_SIZE)
                .fetch();
    }

    public List<ClubMember> getMembersWithId(String slug, Long clubMemberId) {
        return queryFactory
                .select(clubMember)
                .from(clubMember)
                .where(clubMember.club.slug.eq(slug)
                        .and(clubMember.clubAuthority.eq(GENERAL))
                        .and(clubMember.id.gt(clubMemberId)))
                .limit(PAGE_SIZE)
                .fetch();
    }
}
