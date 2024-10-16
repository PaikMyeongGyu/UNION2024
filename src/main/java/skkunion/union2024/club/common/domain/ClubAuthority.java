package skkunion.union2024.club.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PRESIDENT: 회장
 * VICE_PRESIDENT: 부회장
 * GENERAL : 일반 멤버
 * MANAGER로 부회장을 해놓고 싶지만, DB내 정렬 순서 때문에 VICE PRESIDENT로 결정
 */
@Getter
@RequiredArgsConstructor
public enum ClubAuthority {
    PRESIDENT("president"),
    VICE_PRESIDENT("vice_president"),
    GENERAL("general");

    public final String authority;
}
