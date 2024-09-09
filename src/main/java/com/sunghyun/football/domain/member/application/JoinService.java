package com.sunghyun.football.domain.member.application;

import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.member.domain.enums.Role;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.domain.service.MemberDuplicationChecker;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.JoinException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final MemberDuplicationChecker memberDuplicationChecker;

    @Transactional(readOnly = true)
    public boolean checkEmailDuplication(String email){
        Optional<Member> selectedMember = memberRepository.findByEmail(email);

        if(selectedMember.isPresent()){
            return true;
        }
        return false;
    }

    @Transactional
    public MemberJoinResDto join(MemberJoinReqDto memberJoinReqDto) {
        if(memberDuplicationChecker.checkDuplicatiedFields(memberJoinReqDto)){
            throw new JoinException(ErrorCode.DUPLICATED_REGISTER);
        }

        //여기다 도메인 생성을 두자니 난잡한데...
        //그렇다고 도메인생성을 도메인에게 위임하고 MemberJoinReqDto를 파라미터로 넘기자니 패키지 양방향 의존관계되서 안된다..
        Member member = Member.builder()
                .tel(memberJoinReqDto.getTel())
                .birthDt(memberJoinReqDto.getBirthDt())
                .email(memberJoinReqDto.getEmail())
                .gender(memberJoinReqDto.getGender())
                .name(memberJoinReqDto.getName())
                .level(MemberLevelType.ROOKIE)
                .pwd(memberJoinReqDto.getPwd())
                .role(Role.USER)
                .build()
                ;

        Member savedMember = memberRepository.save(member);

        return MemberJoinResDto.from(savedMember);
    }

}
