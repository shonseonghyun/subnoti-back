package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchApplyInSameTimeException extends MatchException{
    public MatchApplyInSameTimeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
