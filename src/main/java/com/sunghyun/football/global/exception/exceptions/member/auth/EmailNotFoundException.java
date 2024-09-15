package com.sunghyun.football.global.exception.exceptions.member.auth;

import org.springframework.security.core.AuthenticationException;

public class EmailNotFoundException extends AuthenticationException {
    public EmailNotFoundException(String code) {
        super(code);
    }
}
