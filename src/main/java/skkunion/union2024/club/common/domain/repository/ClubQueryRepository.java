package skkunion.union2024.club.common.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.QueryHint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.dto.response.ClubMemberDto;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static skkunion.union2024.club.common.domain.ClubAuthority.*;
import static skkunion.union2024.club.common.domain.QClubMember.clubMember;

@Repository
@RequiredArgsConstructor
public class ClubQueryRepository {

    private final int PAGE_SIZE = 16;
    private final JPAQueryFactory queryFactory;

//    @QueryHints(@QueryHint(name = "org.hibernate.comment", value = "USE INDEX (idx_club_authority)"))
//    public List<ClubMemberDto> getMembersWithoutId(String slug) {
//        return queryFactory
//                .select(Projections.constructor(ClubMemberDto.class,
//                        clubMember.id,
//                        clubMember.nickName,
//                        clubMember.clubAuthority,
//                        clubMember.joinedAt
//                ))
//                .from(clubMember)
//                .where(clubMember.club.slug.eq(slug)
//                        .and(clubMember.clubAuthority.in(PRESIDENT, MANAGER, GENERAL)))
//                .limit(PAGE_SIZE)
//                .fetch();
//    }

    //    @QueryHints(@QueryHint(name = "org.hibernate.comment", value = "USE INDEX (idx_club_authority)"))
//    public List<ClubMemberDto> getMembersWithId(String slug, Long clubMemberId) {
//        return queryFactory
//                .select(Projections.constructor(ClubMemberDto.class,
//                        clubMember.id,
//                        clubMember.nickName,
//                        clubMember.clubAuthority,
//                        clubMember.joinedAt
//                ))
//                .from(clubMember)
//                .where(clubMember.club.slug.eq(slug)
//                        .and(clubMember.clubAuthority.eq(GENERAL))
//                        .and(clubMember.id.gt(clubMemberId)))
//                .limit(PAGE_SIZE)
//                .fetch();
//    }

    @Autowired
    private EntityManager entityManager;

    public List<ClubMemberDto> getMembersWithoutId(String slug) {
        String sql = "SELECT club_member_id, nick_name, club_authority, joined_at " +
                "FROM club_member USE INDEX (idx_club_authority) " +
                "WHERE slug = :slug " +
                "AND club_authority IN ('PRESIDENT', 'MANAGER', 'GENERAL') " +
                "LIMIT :pageSize";

        Query query = entityManager.createNativeQuery(sql)
                .setParameter("slug", slug)
                .setParameter("pageSize", PAGE_SIZE);

        List<Object[]> results = query.getResultList();

        // Map the result to ClubMemberDto
        List<ClubMemberDto> members = results.stream().map(row ->
                new ClubMemberDto(
                        (Long) row[0], // club_member_id
                        (String) row[1],                   // nick_name
                        ClubAuthority.valueOf((String) row[2]), // club_authority
                        ((Timestamp) row[3]).toLocalDateTime()                 // joined_at
                )
        ).collect(Collectors.toList());

        return members;
    }

    public List<ClubMemberDto> getMembersWithId(String slug, Long clubMemberId) {
        String sql = "SELECT club_member_id, nick_name, club_authority, joined_at " +
                "FROM club_member USE INDEX (idx_club_authority) " +
                "WHERE slug = :slug " +
                "AND club_authority = 'GENERAL' " +
                "AND club_member_id > :clubMemberId " +
                "LIMIT :pageSize";

        Query query = entityManager.createNativeQuery(sql)
                .setParameter("slug", slug)
                .setParameter("clubMemberId", clubMemberId)
                .setParameter("pageSize", PAGE_SIZE);

        List<Object[]> results = query.getResultList();

        // Map the result to ClubMemberDto
        List<ClubMemberDto> members = results.stream().map(row ->
                new ClubMemberDto(
                        (Long) row[0],    // club_member_id
                        (String) row[1],                      // nick_name
                        ClubAuthority.valueOf((String) row[2]), // club_authority
                        ((Timestamp) row[3]).toLocalDateTime()  // joined_at
                )
        ).collect(Collectors.toList());

        return members;
    }
}
