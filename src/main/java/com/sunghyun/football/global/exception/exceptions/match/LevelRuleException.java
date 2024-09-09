package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class LevelRuleException extends MatchException{
    public LevelRuleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
