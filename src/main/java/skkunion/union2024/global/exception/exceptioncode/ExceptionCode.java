package skkunion.union2024.global.exception.exceptioncode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    // 1000번 대(기본, 계정 관련) 코드
    INVALID_REQUEST(BAD_REQUEST, 1000, "올바르지 않은 요청입니다."),
    ACCOUNT_NOT_FOUND(NOT_FOUND, 1001, "요청한 내용과 일치하는 계정 정보를 찾지 못했습니다."),
    ACCESS_TOKEN_EXPIRED(UNAUTHORIZED,1002, "임시 토큰 정보가 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(UNAUTHORIZED, 1003, "재발급 토큰 정보가 만료되었습니다."),
    AUTHORIZATION_NOT_MATCH(FORBIDDEN, 1004, "해당 계정은 접근 권한이 없습니다."),
    REQUEST_LOGIN(UNAUTHORIZED, 1005, "해당 계정은 접근 권한이 없습니다."),
    EMAIL_VERIFICATION_NOT_FOUND(NOT_FOUND, 1006, "이메일 인증 정보를 찾지 못했습니다."),
    EMAIL_VERIFICATION_EXPIRED(NOT_FOUND, 1007, "이메일 인증 정보가 만료됐습니다. 인증을 재요청해주세요."),
    ACCOUNT_INFO_DOES_NOT_MATCH(UNAUTHORIZED, 1008, "계정과 비밀번호가 일치하지 않습니다."),
    ACCOUNT_ALREADY_EXIST(BAD_REQUEST, 1009, "이미 만들어진 계정이 존재합니다."),

    // 2000번 대(동아리 관련 코드)
    CLUB_NAME_ALREADY_EXIST(BAD_REQUEST, 2001, "이미 존재하는 이름의 동아리입니다.");
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
