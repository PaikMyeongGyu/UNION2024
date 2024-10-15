package skkunion.union2024.club.dto.response;

import java.util.List;
import java.util.Map;

import skkunion.union2024.club.common.domain.ClubAuthority;

public record ClubMemberResponse(
        String clubName,
        ClubAuthority authority,
        Long nextId,
        Long totalMembers,
        Boolean hasNext,
        Map<ClubAuthority, List<ClubMemberSelectDto>> clubMembers
) {
}