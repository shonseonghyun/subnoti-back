package com.sunghyun.football.domain.member.domain;

import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.domain.service.MemberDuplicationChecker;
import com.sunghyun.football.domain.member.infrastructure.MemberRepositoryImpl;
import com.sunghyun.football.domain.member.infrastructure.entity.MemberEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class MemberDuplicationCheckerTest {

    @InjectMocks
    private MemberDuplicationChecker target;

    @Mock
    private MemberRepositoryImpl memberRepository;

    final String email = "sd@a.com";
    final String tel = "01012341234";
    final String name = "길동";

    @DisplayName("이메일 또는 전화번호 하나만 중복 시 true 리턴")
    @Test
    void duplicatedTelOrEmail(){
        MemberJoinReqDto memberJoinReqDto = MemberJoinReqDto.builder()
                .tel(tel)
                .name(name)
                .email(email)
                .build()
                ;
        doReturn(Optional.of(MemberEntity.from(member()))).when(memberRepository).findByEmail(email);
        doReturn(Optional.empty()).when(memberRepository).findByTel(tel);

        Assertions.assertThat(target.checkDuplicatiedFields(memberJoinReqDto)).isEqualTo(true);
    }

    @DisplayName("이메일과 전화번호 중복이 아닐 시 false 리턴")
    @Test
    void checkNotDuplicatedTelAndEmail(){
        MemberJoinReqDto memberJoinReqDto = MemberJoinReqDto.builder()
                .tel(tel)
                .name(name)
                .email(email)
                .build()
                ;
        doReturn(Optional.empty()).when(memberRepository).findByEmail(email);
        doReturn(Optional.empty()).when(memberRepository).findByTel(tel);

        Assertions.assertThat(target.checkDuplicatiedFields(memberJoinReqDto)).isEqualTo(false);
    }

    private Member member() {
        return Member.builder()
                .email(email)
                .tel(tel)
                .name(name)
                .build();
    }
}