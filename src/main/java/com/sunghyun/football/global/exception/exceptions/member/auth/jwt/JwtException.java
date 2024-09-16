package com.sunghyun.football.global.exception.exceptions.member.auth.jwt;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class JwtException extends AppException {
    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
