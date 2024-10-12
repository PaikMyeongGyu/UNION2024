package skkunion.union2024.club.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skkunion.union2024.club.common.domain.repository.ClubMemberRepository;
import skkunion.union2024.club.common.domain.repository.ClubQueryRepository;
import skkunion.union2024.club.common.domain.repository.ClubRepository;
import skkunion.union2024.club.manage.service.servicedto.ClubJoinDto;
import skkunion.union2024.member.service.MemberService;

import lombok.RequiredArgsConstructor;

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
