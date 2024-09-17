package skkunion.union2024.club.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * President: 회장
 * Vice President : 부회장
 * General : 일반 멤버
 */
@Getter
@RequiredArgsConstructor
public enum ClubAuthority {
    PRESIDENT("president"),
    MANAGER("manager"),
    GENERAL("general");

    public final String authority;
}
