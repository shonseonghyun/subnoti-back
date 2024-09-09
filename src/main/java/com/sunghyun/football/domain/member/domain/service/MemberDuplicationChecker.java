package com.sunghyun.football.domain.member.domain.service;

import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.infrastructure.MemberRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDuplicationChecker {
    private final MemberRepositoryImpl memberRepository;

    public boolean checkDuplicatiedFields(MemberJoinReqDto memberJoinReqDto){
        return checkDuplicatedTel(memberJoinReqDto.getTel()) || checkDuplicatedEmail(memberJoinReqDto.getEmail());
    }

    private boolean checkDuplicatedTel(String tel){
        if(memberRepository.findByTel(tel).isPresent()){
            log.error("Tel: {} - 이미 존재하는 번호입니다", tel );
            return true;
        }
        return false;
    }

    private boolean checkDuplicatedEmail(String email){
        if(memberRepository.findByEmail(email).isPresent()){
            log.error("Tel: {} - 이미 존재하는 이메일 입니다", email );
            return true;
        }
        return false;
    }
}
