package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class PlayStatusNotFoundException extends MatchException{
    public PlayStatusNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
