package com.sunghyun.football.domain.member.infrastructure.auth.UserDetails;

import com.sunghyun.football.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

    private Long memberNo;
    private String email;
    private String pwd;
    private String name;
    private List<GrantedAuthority> authorities;
    public static CustomUserDetails from(Member member){
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.memberNo=member.getMemberNo();
        customUserDetails.email=member.getEmail();
        customUserDetails.pwd=member.getPwd();
        customUserDetails.name=member.getName();
//        customUserDetails.authorities = Arrays.asList(member.getRole());
        return customUserDetails;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
