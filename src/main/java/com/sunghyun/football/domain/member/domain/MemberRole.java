package com.sunghyun.football.domain.member.domain;

import com.sunghyun.football.domain.member.domain.enums.Role;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRole {
    private Long roleNo;


    private Role role;

    public MemberRole(Role role) {
        this.role = role;
    }
}
