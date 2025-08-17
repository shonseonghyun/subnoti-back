package com.sunghyun.football.global.exception.member.exception;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class JoinException extends AppException {
    public JoinException(ErrorType errorType) {
        super(errorType);
    }
}
