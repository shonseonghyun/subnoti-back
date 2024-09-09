package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.ErrorCode;

public class GenderRuleException extends MatchException{
    public GenderRuleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
