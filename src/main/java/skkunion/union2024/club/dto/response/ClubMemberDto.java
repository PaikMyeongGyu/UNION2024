package skkunion.union2024.club.dto.response;

import lombok.NoArgsConstructor;
import skkunion.union2024.club.common.domain.ClubAuthority;

import java.time.LocalDateTime;

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
