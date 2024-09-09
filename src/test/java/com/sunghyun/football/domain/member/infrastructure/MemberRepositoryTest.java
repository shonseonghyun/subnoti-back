package com.sunghyun.football.domain.member.infrastructure;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.member.domain.repository.MemberRepository;
import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.repository.TestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class MemberRepositoryTest extends TestRepository {

    @Autowired
    private MemberRepository memberRepository;

    final String email = "sunghyun7895@naver.com";
    final String pwd = "1234";
    final String name = "홍길동";
    final String birthDt = "19950204";
    final Gender gender = Gender.MALE;

    @DisplayName("memberRepository 은 null 아님")
    @Test
    void MemberRepositoryIsNotNull(){
        Assertions.assertThat(memberRepository).isNotNull();
    }




    @DisplayName("Member 등록")
    @Test
    void regMember(){
        //given
        Member member = member();

        //when
        Member saveMember = memberRepository.save(member);

        //then
        Assertions.assertThat(saveMember.getName()).isEqualTo(name);
        Assertions.assertThat(saveMember.getGender()).isEqualTo(gender);
    }

    @DisplayName("Member 조회")
    @Test
    void getMember(){
        //given
        Member member = member();
        Member saveMember = memberRepository.save(member);

        //when
        Member selectedMember =memberRepository.findByMemberNo(saveMember.getMemberNo()).get();

        //then
        Assertions.assertThat(selectedMember.getMemberNo()).isNotNull();
    }

    @DisplayName("Member 수정")
    @Test
    void updateMember(){
        //given
        Member member = member();
        Member savedMember =memberRepository.save(member);

        Long memberNo = savedMember.getMemberNo();
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
        Member selectedMember = memberRepository.findByMemberNo(savedMember.getMemberNo()).get();
        selectedMember.updateMember(memberUpdReqDto);
        memberRepository.flush();

        //then
        Assertions.assertThat(selectedMember.getName()).isEqualTo(updName);
        Assertions.assertThat(selectedMember.getLevel()).isEqualTo(level);
    }

    @DisplayName("Member 삭제")
    @Test
    void deleteMember(){
        //given
        Member member = member();
        Member savedMember = memberRepository.save(member);
        Long savedMemberNo = savedMember.getMemberNo();

        //when
        memberRepository.delete(savedMember);

        //then
        Optional<Member> selectedBook = memberRepository.findByMemberNo(savedMemberNo);
        Assertions.assertThat(selectedBook).isNotPresent();
    }

    private Member member(){
        return Member.builder()
                .name(name)
                .email(email)
                .pwd(pwd)
                .birthDt(birthDt)
                .gender(gender)
                .build()
                ;
    }

}