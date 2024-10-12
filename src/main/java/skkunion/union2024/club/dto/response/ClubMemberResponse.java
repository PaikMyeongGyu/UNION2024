package skkunion.union2024.club.dto.response;

import java.util.List;
import java.util.Map;

import skkunion.union2024.club.common.domain.ClubAuthority;

public record ClubMemberResponse(
        String clubName,
        Long totalMembers,
        Boolean hasNext,
        Long nextId,
        Map<ClubAuthority, List<ClubMemberSelectDto>> clubMembers
) {
}