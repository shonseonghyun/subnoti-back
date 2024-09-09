package com.sunghyun.football.global.exception.exceptions.member;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class MemberException extends AppException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
