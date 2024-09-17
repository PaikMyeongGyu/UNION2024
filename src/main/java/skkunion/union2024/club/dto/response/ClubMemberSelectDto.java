package skkunion.union2024.club.dto.response;

import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;

import java.time.LocalDateTime;

public record ClubMemberSelectDto(
        String nickName,
        ClubAuthority authority,
        LocalDateTime joinedAt
) {

}
