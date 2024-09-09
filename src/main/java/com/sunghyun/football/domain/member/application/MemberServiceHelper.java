package com.sunghyun.football.domain.member.application;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceHelper {
    private final MemberRepository memberRepository;

    public Member findExistingMember(Long memberNo){
        Member selectedMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(()-> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return selectedMember;
    }
}
