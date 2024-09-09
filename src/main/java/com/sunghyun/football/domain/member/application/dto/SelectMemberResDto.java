package com.sunghyun.football.domain.member.application.dto;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.member.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectMemberResDto {
    private Long memberNo;

    private String email;

    private String name;

    private String birthDt;

    private Gender gender;

    private String tel;

    private MemberLevelType level;

    private Role role;

    public static SelectMemberResDto from(Member member) {
        SelectMemberResDto selectMemberResDto = new SelectMemberResDto();
        selectMemberResDto.memberNo = member.getMemberNo();
        selectMemberResDto.email = member.getEmail();
        selectMemberResDto.name = member.getName();
        selectMemberResDto.birthDt = member.getBirthDt();
        selectMemberResDto.gender = member.getGender();
        selectMemberResDto.tel = member.getTel();
        selectMemberResDto.level = member.getLevel();
        selectMemberResDto.role = member.getRole();
        return selectMemberResDto;
    }
}

