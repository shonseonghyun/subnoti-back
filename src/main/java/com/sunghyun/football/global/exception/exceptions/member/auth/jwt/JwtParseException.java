package com.sunghyun.football.global.exception.exceptions.member.auth.jwt;

import com.sunghyun.football.global.exception.ErrorCode;

public class JwtParseException extends JwtException{
    public JwtParseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
