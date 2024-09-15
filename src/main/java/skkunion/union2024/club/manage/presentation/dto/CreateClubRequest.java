package skkunion.union2024.club.manage.presentation.dto;

import skkunion.union2024.club.manage.service.request.CreateClubDto;
import skkunion.union2024.member.domain.Member;

public record CreateClubRequest(
        String clubName,
        String presidentName,
        String description
) {
    public CreateClubDto covertToCreateClubDto(Member member) {
        return new CreateClubDto(clubName, presidentName, description, member);
    }
}
