package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchPlayerExistException extends MatchException{
    public MatchPlayerExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
