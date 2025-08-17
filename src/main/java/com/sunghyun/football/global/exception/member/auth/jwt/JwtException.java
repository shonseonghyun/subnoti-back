package com.sunghyun.football.global.exception.member.auth.jwt;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class JwtException extends AppException {
    public JwtException(ErrorType errorType) {
        super(errorType);
    }
}
