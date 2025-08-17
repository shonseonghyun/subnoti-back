package com.sunghyun.football.global.exception.member.auth.jwt;

import com.sunghyun.football.global.exception.ErrorType;

public class JwtExpiredException extends JwtException {
    public JwtExpiredException(ErrorType errorType) {
        super(errorType);
    }
}
