package com.sunghyun.football.domain.member.infrastructure.auth.custom.provider;

import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.auth.PasswordNotMatchException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //아직 인증되지 않은 인증객체
        final Authentication unAuthentication = authentication;

        //이메일(인증객체 필드에서 가져오기)
        final String email = unAuthentication.getName();
        final String pwd = (String)unAuthentication.getCredentials();

        //유저 정보 가져오기
        UserDetails user = this.userDetailsService.loadUserByUsername(email);

        //유저 비밀번호 일치여부 체크
        if(!passwordEncoder.matches(pwd,user.getPassword())){
            throw new PasswordNotMatchException(ErrorCode.PASSWORD_NOT_MATCH.getCode());
        }

        //인증완료된 인증객체 리턴
        return UsernamePasswordAuthenticationToken.authenticated(email,authentication.getCredentials(),user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
