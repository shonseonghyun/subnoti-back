package com.sunghyun.football.domain.member.application.dto;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.MemberRole;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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


    private List<Role> role;

    private SubscriptionInfo subscriptionInfo;

    public static SelectMemberResDto from(Member member) {
        return SelectMemberResDto.builder()
                .memberNo(member.getMemberNo())
                .email(member.getEmail())
                .name(member.getName())
                .birthDt(member.getBirthDt())
                .gender(member.getGender())
                .tel(member.getTel())
                .role(member.getRole().stream().map(MemberRole::getRole).toList())
                .build();
    }

    public static SelectMemberResDto from(Member member,SubscriptionInfo subscriptionInfo) {
        return SelectMemberResDto.builder()
                .memberNo(member.getMemberNo())
                .email(member.getEmail())
                .name(member.getName())
                .birthDt(member.getBirthDt())
                .gender(member.getGender())
                .tel(member.getTel())
                .role(member.getRole().stream().map(MemberRole::getRole).toList())
                .subscriptionInfo(subscriptionInfo)
                .build();
    }
}

