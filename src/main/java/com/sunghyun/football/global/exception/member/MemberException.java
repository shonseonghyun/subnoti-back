package com.sunghyun.football.global.exception.member;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class MemberException extends AppException {
    public MemberException(ErrorType errorType) {
        super(errorType);
    }
}
