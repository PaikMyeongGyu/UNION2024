package skkunion.union2024.club.domain;

import lombok.Getter;

/**
 * President: 회장
 * Vice President : 부회장
 * General : 일반 멤버
 */
@Getter
public enum ClubAuthority {
    PRESIDENT("president"),
    MANAGER("manager"),
    GENERAL("general");

    public final String authority;

    ClubAuthority(String authority) {
        this.authority = authority;
    }
}
