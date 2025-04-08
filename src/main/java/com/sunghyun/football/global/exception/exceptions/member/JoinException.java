package com.sunghyun.football.global.exception.exceptions.member;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class JoinException extends AppException{
    public JoinException(ErrorCode errorCode) {
        super(errorCode);
    }
}
