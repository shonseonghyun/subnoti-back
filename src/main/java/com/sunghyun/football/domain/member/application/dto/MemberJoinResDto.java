package com.sunghyun.football.domain.member.application.dto;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinResDto {
    private Long memberNo;

    private String email;

    private String pwd;

    private String name;

    private String birthDt;

    private Gender gender;

    private String tel;

//    private MemberLevelType level;

    public static MemberJoinResDto from(Member member){
        MemberJoinResDto memberJoinResDto = new MemberJoinResDto();
        memberJoinResDto.memberNo = member.getMemberNo();
        memberJoinResDto.email= member.getEmail();
        memberJoinResDto.tel = member.getTel();
        memberJoinResDto.name=member.getName();
        memberJoinResDto.pwd=member.getPwd();
        memberJoinResDto.birthDt=member.getBirthDt();
        memberJoinResDto.gender = member.getGender();
//        memberJoinResDto.level= member.getLevel();

        return memberJoinResDto;
    }

}