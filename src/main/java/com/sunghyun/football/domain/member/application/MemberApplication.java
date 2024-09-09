package com.sunghyun.football.domain.member.application;

import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberApplication {

    private final MemberRepository memberRepository;
    private final MemberServiceHelper memberServiceHelper;

    @Transactional
    public SelectMemberResDto getMember(Long memberNo) {
        Member selectedMember = memberServiceHelper.findExistingMember(memberNo);

        return SelectMemberResDto.from(selectedMember);
    }

    @Transactional
    public void updateMember(MemberUpdReqDto memberUpdReqDto){
        Member selectedMember = memberServiceHelper.findExistingMember(memberUpdReqDto.getMemberNo());

        memberRepository.save(selectedMember);
    }

    public void deleteMember(Long memberNo) {
        Member selectedMember = memberServiceHelper.findExistingMember(memberNo);

        memberRepository.delete(selectedMember);
    }


}
