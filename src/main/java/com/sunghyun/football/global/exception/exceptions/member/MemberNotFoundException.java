package com.sunghyun.football.global.exception.exceptions.member;

import com.sunghyun.football.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends MemberException {
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
