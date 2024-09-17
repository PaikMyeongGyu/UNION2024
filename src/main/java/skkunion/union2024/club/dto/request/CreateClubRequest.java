package skkunion.union2024.club.dto.request;

import skkunion.union2024.club.manage.service.servicedto.CreateClubDto;
import skkunion.union2024.member.domain.Member;

public record CreateClubRequest(
        String clubSlug,
        String clubName,
        String presidentName,
        String description
) {
    public CreateClubDto covertToCreateClubDto(Member member) {
        return new CreateClubDto(clubSlug, clubName, presidentName, description, member);
    }
}
