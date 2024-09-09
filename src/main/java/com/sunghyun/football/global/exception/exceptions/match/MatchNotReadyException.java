package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchNotReadyException extends MatchException{
    public MatchNotReadyException(ErrorCode errorCode) {
        super(errorCode);
    }
}
