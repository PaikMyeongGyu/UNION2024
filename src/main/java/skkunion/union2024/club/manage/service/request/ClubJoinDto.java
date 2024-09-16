package skkunion.union2024.club.manage.service.request;

import skkunion.union2024.member.domain.Member;

public record ClubJoinDto(
        String clubSlug,
        String nickName,
        Member member
) {
}
