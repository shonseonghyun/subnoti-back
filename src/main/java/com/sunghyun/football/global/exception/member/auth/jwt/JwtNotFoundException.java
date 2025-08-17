package com.sunghyun.football.global.exception.member.auth.jwt;

import com.sunghyun.football.global.exception.ErrorType;

public class JwtNotFoundException extends JwtException {
    public JwtNotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
