package skkunion.union2024.club.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skkunion.union2024.club.common.domain.Club;
import skkunion.union2024.club.common.domain.ClubAuthority;
import skkunion.union2024.club.common.domain.ClubMember;
import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubQueryRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.club.dto.response.ClubMemberDto;
import skkunion.union2024.club.dto.response.ClubMemberResponse;
import skkunion.union2024.club.dto.response.ClubMemberSelectDto;
import skkunion.union2024.club.manage.service.servicedto.ClubJoinDto;
import skkunion.union2024.global.exception.ClubException;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.service.MemberService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ClubSelectService {

    private final int PAGE_SIZE = 15;

    private final MemberService memberService;

    private final ClubRepository clubRepository;
    private final ClubQueryRepository clubQueryRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public void selectClub(ClubJoinDto req) {

    }

}
