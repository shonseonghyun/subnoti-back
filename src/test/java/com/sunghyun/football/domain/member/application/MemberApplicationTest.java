package com.sunghyun.football.domain.member.application;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
import com.sunghyun.football.global.exception.exceptions.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberApplicationTest {

    //단위테스트의 주체
    @InjectMocks
    private MemberApplication target;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberServiceHelper memberServiceHelper;

    final String email = "abcab@naver.com";
    final String tel = "01012341234";
    final String name = "길동";


    @DisplayName("업데이트 경우 존재하지 않는 회원번호 인입 시 예외")
    @Test
    void updateMemberWithNotExistedMemberNo(){
        Long memberNo = 1L;
        String updName = "업데이트이름";
        String updPwd = "1234";
        MemberLevelType level = MemberLevelType.AMATEUR1;

        MemberUpdReqDto memberUpdReqDto = MemberUpdReqDto.builder()
                .memberNo(memberNo)
                .name(updName)
                .pwd(updPwd)
                .memberLevelType(level)
            .build()
            ;

        doThrow(MemberNotFoundException.class).when(memberServiceHelper).findExistingMember(memberUpdReqDto.getMemberNo());
//        when(memberServiceHelper.findExistingMember(memberUpdReqDto.getMemberNo())).thenReturn(Optional.empty());

        //then
        Assertions.assertThatThrownBy(()->target.updateMember(memberUpdReqDto)).isInstanceOf(MemberNotFoundException.class);
    }
    
    @DisplayName("업데이트 정상 처리")
    @Test
    void updateMember(){
        Long memberNo = 1L;
        String updName = "업데이트이름";
        String updPwd = "1234";
        MemberLevelType level = MemberLevelType.AMATEUR1;
        MemberUpdReqDto memberUpdReqDto = MemberUpdReqDto.builder()
                .memberNo(memberNo)
                .name(updName)
                .pwd(updPwd)
                .memberLevelType(level)
                .build()
                ;

        Member member = createMember();
        doReturn(member).when(memberServiceHelper).findExistingMember(memberNo);

        target.updateMember(memberUpdReqDto);

        verify(memberRepository).save(member);

    }


    @DisplayName("삭제 예외 - 존재하지 않는 멤버 번호 인입")
    @Test
    void deleteWithWrongMemberNo(){
        //given
        Long memberNo = 1L;
        String updName = "업데이트이름";
        String updPwd = "1234";
        MemberLevelType level = MemberLevelType.AMATEUR1;
        MemberUpdReqDto memberUpdReqDto = MemberUpdReqDto.builder()
                .memberNo(memberNo)
                .name(updName)
                .pwd(updPwd)
                .memberLevelType(level)
                .build()
                ;

        //when
        doThrow(MemberNotFoundException.class).when(memberServiceHelper).findExistingMember(memberNo);

        //then
        Assertions.assertThatThrownBy(()->target.updateMember(memberUpdReqDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("멤버 삭제 정상 처리")
    @Test
    void deleteMember(){
        //given
        Long memberNo = 1L;
        Member member= createMember();

        //when
        doReturn(member).when(memberServiceHelper).findExistingMember(memberNo);

        //then
        Assertions.assertThatCode(()->target.deleteMember(memberNo)).doesNotThrowAnyException();
        verify(memberRepository).delete(member);
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