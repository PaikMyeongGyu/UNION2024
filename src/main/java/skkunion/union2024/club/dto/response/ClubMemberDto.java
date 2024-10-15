package skkunion.union2024.club.dto.response;

import java.time.LocalDateTime;
import java.util.Comparator;

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

    public static final Comparator<ClubMemberDto> COMP_AUTH_JOINED_AT_DESC = Comparator
            .comparing(ClubMemberDto::authority)
            .thenComparing(Comparator.comparing(ClubMemberDto::joinedAt).reversed());
}
