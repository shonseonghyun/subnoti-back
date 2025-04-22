package com.sunghyun.football.domain.member.domain.dto;

import com.sunghyun.football.domain.member.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdReqDto {
    private String name;

    private String tel;

    private String pwd;

    private String pwdConfirm;

    private Gender gender;
}
