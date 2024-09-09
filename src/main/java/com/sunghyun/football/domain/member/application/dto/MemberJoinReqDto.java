package com.sunghyun.football.domain.member.application.dto;

import com.sunghyun.football.domain.member.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinReqDto {

    @Email
    @NotNull
    private String email;

    private String pwd;

    @NotNull
    private String name;

    @NotNull
    private String birthDt;

    @NotNull
    private Gender gender;

    @NotNull
    private String tel;
}
