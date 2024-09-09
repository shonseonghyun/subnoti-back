package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchNotFoundException extends MatchException{
    public MatchNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
