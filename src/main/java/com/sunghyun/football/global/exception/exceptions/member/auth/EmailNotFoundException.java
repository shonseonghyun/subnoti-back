package com.sunghyun.football.global.exception.exceptions.member.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailNotFoundException extends UsernameNotFoundException {
    public EmailNotFoundException(String code) {
        super(code);
    }
}
