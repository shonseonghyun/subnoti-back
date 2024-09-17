package com.sunghyun.football.global.exception.exceptions.member.auth.jwt;

import com.sunghyun.football.global.exception.ErrorCode;

public class JwtNotFoundException extends JwtException{
    public JwtNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
