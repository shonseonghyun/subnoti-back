package com.sunghyun.football.domain.member.infrastructure.auth.dto;

import com.sunghyun.football.domain.member.infrastructure.auth.UserDetails.CustomUserDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginResDto {
    private Long memberNo;
    private String email;

    public static MemberLoginResDto from(CustomUserDetails customUserDetails){
        MemberLoginResDto memberLoginResDto = new MemberLoginResDto();
        memberLoginResDto.email=customUserDetails.getUsername();
        memberLoginResDto.memberNo=customUserDetails.getMemberNo();
        return memberLoginResDto;
    }
}
