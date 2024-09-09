package com.sunghyun.football.global.exception.exceptions.member;

import com.sunghyun.football.global.exception.ErrorCode;

public class MemberLevelNotFoundException extends MemberException{
    public MemberLevelNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
