package com.sunghyun.football.domain.member.domain;


import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.member.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Member {
    private Long memberNo;

    private String email;

    private String pwd;

    private String name;

    private String birthDt;

    private Gender gender;

    private String tel;

    private MemberLevelType level;

    private Role role;

    public void updateMember(MemberUpdReqDto memberUpdReqDto){
        if(!(memberUpdReqDto.getMemberLevelType()==null)){
            changeMemberLevel(memberUpdReqDto.getMemberLevelType());
        }
        if(!(memberUpdReqDto.getName().isEmpty())){
            changeName(memberUpdReqDto.getName());
        }
        if(!(memberUpdReqDto.getPwd().isEmpty())){
            changePwd(memberUpdReqDto.getPwd());
        }
        if(!(memberUpdReqDto.getGender()==null)){
            changeGender(memberUpdReqDto.getGender());
        }
    }

    private void changeName(String name){
        this.name = name;
    }

    private void changeGender(Gender gender){
        this.gender= gender;
    }

    private void changePwd(String pwd){
        this.pwd= pwd;
    }

    private void changeMemberLevel(MemberLevelType level){
        this.level= level;
    }
}
