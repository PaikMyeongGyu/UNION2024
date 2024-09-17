package skkunion.union2024.club.dto.request;

import skkunion.union2024.club.manage.service.servicedto.ClubJoinDto;
import skkunion.union2024.member.domain.Member;

public record ClubJoinRequest(
        String clubSlug,
        String nickName
) {
    public ClubJoinDto convertToClubJoinDto(Member member) {
        return new ClubJoinDto(clubSlug, nickName, member);
    }
}
