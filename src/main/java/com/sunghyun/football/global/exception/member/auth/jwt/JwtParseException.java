package com.sunghyun.football.global.exception.member.auth.jwt;

import com.sunghyun.football.global.exception.ErrorType;

public class JwtParseException extends JwtException {
    public JwtParseException(ErrorType errorType) {
        super(errorType);
    }
}
