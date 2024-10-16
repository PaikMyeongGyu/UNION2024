package skkunion.union2024.club.common.domain.repository;

import static skkunion.union2024.club.common.domain.ClubAuthority.*;
import static skkunion.union2024.club.common.domain.QClubMember.clubMember;
import static skkunion.union2024.club.dto.response.ClubMemberDto.COMP_AUTH_JOINED_AT_DESC;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.stereotype.Repository;

import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.dto.response.ClubMemberDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClubQueryRepository {

    private final int PAGE_SIZE = 16;
    private final JPAQueryFactory queryFactory;

    public List<ClubMemberDto> getClubMembers(String slug) {
        return getMembers(slug);
    }

    public List<ClubMemberDto> getClubMembers(String slug, ClubAuthority authority, Long memberId) {
        if (authority.equals(VICE_PRESIDENT)) {
            List<ClubMemberDto> members = new ArrayList<>();
            members.addAll(getManagers(slug, memberId));
            members.addAll(getGenerals(slug, null));

            return members.stream()
                            .sorted(COMP_AUTH_JOINED_AT_DESC)
                            .limit(PAGE_SIZE)
                            .toList();
        }

        return getGenerals(slug, memberId);
    }

    private List<ClubMemberDto> getManagers(String slug, Long managerId) {
        BooleanBuilder dynamicManagerIdLt = new BooleanBuilder();

        if (managerId != null) {
            dynamicManagerIdLt.and(clubMember.id.lt(managerId));
        }

        return queryFactory.select(Projections.constructor(ClubMemberDto.class,
                        clubMember.id,
                        clubMember.nickName,
                        clubMember.clubAuthority,
                        clubMember.joinedAt
                ))
                .from(clubMember)
                .where(clubMember.club.slug.eq(slug),
                        clubMember.clubAuthority.eq(VICE_PRESIDENT),
                        dynamicManagerIdLt)
                .orderBy(clubMember.club.slug.asc(), clubMember.clubAuthority.asc(), clubMember.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    private List<ClubMemberDto> getGenerals(String slug, Long generalId) {
        BooleanBuilder dynamicGeneralIdLt = new BooleanBuilder();
        if (generalId != null) {
            dynamicGeneralIdLt.and(clubMember.id.lt(generalId));
        }

        return queryFactory.select(Projections.constructor(ClubMemberDto.class,
                        clubMember.id,
                        clubMember.nickName,
                        clubMember.clubAuthority,
                        clubMember.joinedAt
                ))
                .from(clubMember)
                .where(clubMember.club.slug.eq(slug),
                        clubMember.clubAuthority.eq(GENERAL),
                        dynamicGeneralIdLt)
                .orderBy(clubMember.club.slug.asc(), clubMember.clubAuthority.asc(), clubMember.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    private List<ClubMemberDto> getMembers(String slug) {
        return queryFactory
                .select(Projections.constructor(ClubMemberDto.class,
                        clubMember.id,
                        clubMember.nickName,
                        clubMember.clubAuthority,
                        clubMember.joinedAt
                ))
                .from(clubMember)
                .where(clubMember.club.slug.eq(slug))
                .orderBy(clubMember.club.slug.asc(), clubMember.clubAuthority.asc(), clubMember.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

}
