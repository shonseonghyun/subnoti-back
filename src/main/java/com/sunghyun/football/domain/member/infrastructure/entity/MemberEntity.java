package com.sunghyun.football.domain.member.infrastructure.entity;

import com.sunghyun.football.domain.match.infrastructure.entity.converter.MemberLevelTypeConverter;
import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import com.sunghyun.football.domain.member.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
@ToString
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    private String email;

    private String pwd;

    private String name;

    private String birthDt;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String tel;

    @Convert(converter = MemberLevelTypeConverter.class)
    private MemberLevelType level;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public MemberEntity(String email, String pwd, String name, String birthDt, Gender gender, String tel) {
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.birthDt = birthDt;
        this.gender = gender;
        this.tel = tel;
        this.level = MemberLevelType.ROOKIE;
        this.role = Role.USER;
    }


    public static MemberEntity from(Member member){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.memberNo = member.getMemberNo();
        memberEntity.email = member.getEmail();
        memberEntity.pwd = member.getPwd();;
        memberEntity.name = member.getName();
        memberEntity.birthDt = member.getBirthDt();
        memberEntity.gender = member.getGender();
        memberEntity.tel = member.getTel();
        memberEntity.level = member.getLevel();
        memberEntity.role = member.getRole();
        return memberEntity;
    }

    public Member toModel(){
        return Member.builder()
                .memberNo(memberNo)
                .email(email)
                .pwd(pwd)
                .birthDt(birthDt)
                .name(name)
                .tel(tel)
                .gender(gender)
                .level(level)
                .role(role)
                .build()
                ;
    }
}
