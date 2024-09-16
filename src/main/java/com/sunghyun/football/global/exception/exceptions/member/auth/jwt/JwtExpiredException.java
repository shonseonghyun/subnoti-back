package com.sunghyun.football.global.exception.exceptions.member.auth.jwt;

import com.sunghyun.football.global.exception.ErrorCode;

public class JwtExpiredException extends JwtException{
    public JwtExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
