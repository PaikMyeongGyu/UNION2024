package skkunion.union2024.club.dto.response;

import skkunion.union2024.club.common.domain.ClubAuthority;

import java.util.List;
import java.util.Map;

public record ClubMemberResponse(
        String clubName,
        Long totalMembers,
        Boolean hasNext,
        Long nextId,
        Map<ClubAuthority, List<ClubMemberSelectDto>> clubMembers
) {
}