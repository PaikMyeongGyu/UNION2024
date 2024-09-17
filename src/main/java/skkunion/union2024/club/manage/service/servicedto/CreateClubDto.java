package skkunion.union2024.club.manage.service.servicedto;

import skkunion.union2024.member.domain.Member;

public record CreateClubDto(
        String clubSlug,
        String clubName,
        String presidentName,
        String description,
        Member member) {
}
