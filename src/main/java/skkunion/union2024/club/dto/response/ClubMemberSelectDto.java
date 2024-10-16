package skkunion.union2024.club.dto.response;

import java.time.LocalDateTime;

import skkunion.union2024.club.common.domain.ClubAuthority;

public record ClubMemberSelectDto(
        String nickName,
        ClubAuthority authority,
        LocalDateTime joinedAt
) {
}
