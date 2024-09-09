package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchPlayerNotFoundException extends MatchException{
    public MatchPlayerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
