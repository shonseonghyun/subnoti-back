package com.sunghyun.football.domain.member.infrastructure.entity;

import com.sunghyun.football.domain.member.domain.MemberRole;
import com.sunghyun.football.domain.member.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MemberRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleNo;

//    @ManyToOne
//    @JoinColumn(name = "member_no")
//    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    private Role role;

    public MemberRole toModel(){
        return MemberRole.builder()
                .roleNo(roleNo)
                .role(role)
                .build()
                ;
    }

    public static MemberRoleEntity from(MemberRole memberRole){
        MemberRoleEntity memberRoleEntity = new MemberRoleEntity();
        memberRoleEntity.roleNo = memberRole.getRoleNo();
        memberRoleEntity.role= memberRole.getRole();
        return memberRoleEntity;
    }
}
