package skkunion.union2024.club.dto.response;

import java.time.LocalDateTime;

import skkunion.union2024.club.common.domain.ClubAuthority;

public record ClubMemberDto(
        Long id,
        String nickName,
        ClubAuthority authority,
        LocalDateTime joinedAt
) {
    public ClubMemberSelectDto convertToClubMemberDto() {
        return new ClubMemberSelectDto(nickName,
                                       authority,
                                       joinedAt);
    }
}
