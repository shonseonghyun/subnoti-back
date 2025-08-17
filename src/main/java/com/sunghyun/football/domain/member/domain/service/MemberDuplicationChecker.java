package com.sunghyun.football.domain.member.domain.service;

import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.infrastructure.MemberRepositoryImpl;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.member.exception.JoinException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDuplicationChecker {
    private final MemberRepositoryImpl memberRepository;

    public void checkDuplicatiedFields(MemberJoinReqDto memberJoinReqDto){
//        checkDuplicatedTel(memberJoinReqDto.getTel());
        checkDuplicatedEmail(memberJoinReqDto.getEmail());
    }

    private void checkDuplicatedTel(String tel){
        memberRepository.findByTel(tel)
                .ifPresent(m -> {
                    throw new JoinException(ErrorType.DUPLICATED_TEL_REGISTER);
                });
    }

    private void checkDuplicatedEmail(String email){
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new JoinException(ErrorType.DUPLICATED_EMAIL_REGISTER);
                });
    }
}
