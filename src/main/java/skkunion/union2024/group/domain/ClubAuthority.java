package skkunion.union2024.group.domain;

import lombok.Getter;

/**
 * President: 회장
 * Vice President : 부회장
 * General : 일반 멤버
 */
@Getter
public enum ClubAuthority {
    PRESIDENT("president"),
    VICE_PRESIDENT("vice_president"),
    GENERAL("general");

    public final String authority;

    ClubAuthority(String authority) {
        this.authority = authority;
    }
}
