package com.sunghyun.football.domain.member.domain.dto;

import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@AllArgsConstructor
public class MemberUpdReqDto {
    private Long memberNo;
    private String name;
    private String pwd;
    private MemberLevelType memberLevelType;
    private Gender gender;
}
