package com.sunghyun.football.global.exception.member.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.member.MemberException;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends MemberException {
    public MemberNotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
