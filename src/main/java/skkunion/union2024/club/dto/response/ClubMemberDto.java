package skkunion.union2024.club.dto.response;

import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;

import java.time.LocalDateTime;

public record ClubMemberDto(
        String nickName,
        ClubAuthority authority,
        LocalDateTime joinedAt
) {
    public static ClubMemberDto convertToClubMemberDto(ClubMember clubMember) {
        return new ClubMemberDto(clubMember.getNickName(),
                                 clubMember.getClubAuthority(),
                                 clubMember.getJoinedAt());
    }
}
