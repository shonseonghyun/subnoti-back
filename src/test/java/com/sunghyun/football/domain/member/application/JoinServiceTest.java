package com.sunghyun.football.domain.member.application;

import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.domain.service.MemberDuplicationChecker;
import com.sunghyun.football.global.exception.exceptions.member.JoinException;
import org.assertj.core.api.Assertions;
import org.hibernate.mapping.Join;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JoinServiceTest {

    @InjectMocks
    private JoinService target;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberDuplicationChecker memberDuplicationChecker;

    final String email = "abcab@naver.com";
    final String tel = "01012341234";
    final String name = "길동";


    @DisplayName("이메일 중복 확인 - 중복확인 존재 시 true 리턴")
    @Test
    void checkEmailDuplicationTrue(){

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(createMember()));

        boolean flg = target.checkEmailDuplication(email);

        Assertions.assertThat(flg).isEqualTo(true);
    }

    @DisplayName("이메일 중복 확인 - 중복확인 존재하지 않을 시 false 리턴")
    @Test
    void checkEmailDuplicationFalse(){

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean flg = target.checkEmailDuplication(email);

        Assertions.assertThat(flg).isEqualTo(false);
    }

    @DisplayName("회원가입 예외 - 이미 존재하는 휴대폰번호,이메일")
    @Test
    void joinWithTelThatAlreadyExists(){
        MemberJoinReqDto memberJoinReqDto = MemberJoinReqDto.builder()
                .tel(tel)
                .build()
                ;

        //given
//        doReturn(Optional.of(MemberEntity.builder().build())).when(memberRepository).findByTel(tel);
        doReturn(true).when(memberDuplicationChecker).checkDuplicatiedFields(memberJoinReqDto);

        Assertions.assertThatThrownBy(()->target.join(memberJoinReqDto)).isInstanceOf(JoinException.class);
    }

    @DisplayName("회원가입 정상 처리")
    @Test
    void joinSuccess(){
        MemberJoinReqDto memberJoinReqDto = MemberJoinReqDto.builder()
                .email(email)
                .name(name)
                .tel(tel)
                .build()
                ;

        doReturn(false).when(memberDuplicationChecker).checkDuplicatiedFields(memberJoinReqDto);
        doReturn(createMember()).when(memberRepository).save(any(Member.class));

        MemberJoinResDto memberJoinResDto = target.join(memberJoinReqDto);

        Assertions.assertThat(memberJoinResDto.getEmail()).isEqualTo(email);
        Assertions.assertThat(memberJoinResDto.getName()).isEqualTo(name);

        verify(memberRepository,times(1)).save(any(Member.class));
        verify(memberDuplicationChecker,times(1)).checkDuplicatiedFields(memberJoinReqDto);
    }

    // 테스트에서 사용할 가짜 Member 객체 생성 메서드
    private Member createMember() {
        return Member.builder()
                .email(email)
                .tel(tel)
                .name(name)
                .build();
    }
}