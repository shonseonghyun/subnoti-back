package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchAlreadyRegSameTimeException extends MatchException{
    public MatchAlreadyRegSameTimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
