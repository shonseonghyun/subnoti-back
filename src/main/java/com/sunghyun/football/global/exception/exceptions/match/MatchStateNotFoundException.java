package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchStateNotFoundException extends MatchException{
    public MatchStateNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
