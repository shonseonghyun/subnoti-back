package com.sunghyun.football.domain.member.domain;


import com.sunghyun.football.domain.member.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class Member {
    private Long memberNo;

    private String email;

//    @Merge
    private String pwd;

//    @Merge
    private String name;

//    @Merge
    private String birthDt;

//    @Merge
    private Gender gender;

//    @Merge
    private String tel;

//    private MemberLevelType level;

    private List<MemberRole> role;

    public void updateMember(final String name, final Gender gender, final String tel){
        if (StringUtils.hasText(name)) {
            changeName(name);
        }

        if (gender != null) {
            changeGender(gender);
        }

        if (StringUtils.hasText(tel)) {
            changeTel(tel);
        }
    }

    private void changeName(String name){
        this.name = name;
    }

    private void changeGender(Gender gender){
        this.gender= gender;
    }

    public void changePwd(String pwd){
        this.pwd= pwd;
    }

    private void changeTel(String tel){
        this.tel= tel;
    }
}
