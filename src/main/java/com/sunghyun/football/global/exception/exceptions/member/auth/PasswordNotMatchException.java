package com.sunghyun.football.global.exception.exceptions.member.auth;

import org.springframework.security.core.AuthenticationException;

public class PasswordNotMatchException extends AuthenticationException {
    public PasswordNotMatchException(String code) {
        super(code);
    }
}
