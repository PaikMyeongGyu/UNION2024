package skkunion.union2024.member.domain;

/**
 * UNVERIFIED : 이메일이 완료되지 않은 계정
 * DORMANT : 기간에 따라서 임시적으로 잠긴 계정
 * ACTIVE : 일반적인 계정
 * BANNED : 신고 횟수가 일정 수준 이상으로 초과돼 벤이 된 유저
 * DELETED : 삭제된 계정, 데이터는 남아있음.
 */
public enum MemberState {
    UNVERIFIED,
    DORMANT,
    ACTIVE,
    BANNED,
    DELETED
}
