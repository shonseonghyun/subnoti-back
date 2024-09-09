package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchStatusNotFoundException extends MatchException{
    public MatchStatusNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
