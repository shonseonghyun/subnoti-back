package com.sunghyun.football.domain.member.infrastructure.auth.UserDetails;

import com.sunghyun.football.domain.member.domain.MemberRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    private MemberRole memberRole;

    @Override
    public String getAuthority() {
        return memberRole.getRole().name();
    }
}
