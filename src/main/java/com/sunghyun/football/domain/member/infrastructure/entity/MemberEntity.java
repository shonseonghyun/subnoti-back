package com.sunghyun.football.domain.member.infrastructure.entity;

import com.sunghyun.football.domain.member.domain.Member;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

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

//    @Convert(converter = MemberLevelTypeConverter.class)
//    private MemberLevelType level;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.REMOVE} /*,orphanRemoval = true */ ) //remove나 orphan 둘중 하나만 있어도 부모 시 자식도 함께 삭제된다..
    @JoinColumn(name = "member_no")
    private List<MemberRoleEntity> role;

    public static MemberEntity from(Member member){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.memberNo = member.getMemberNo();
        memberEntity.email = member.getEmail();
        memberEntity.pwd = member.getPwd();
        memberEntity.name = member.getName();
        memberEntity.birthDt = member.getBirthDt();
        memberEntity.gender = member.getGender();
        memberEntity.tel = member.getTel();
//        memberEntity.level = member.getLevel();
        memberEntity.role = member.getRole().stream().map(MemberRoleEntity::from).collect(Collectors.toList());
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
//                .level(level)
                .role(role.stream().map(MemberRoleEntity::toModel).collect(Collectors.toList()))
                .build()
                ;
    }
}
