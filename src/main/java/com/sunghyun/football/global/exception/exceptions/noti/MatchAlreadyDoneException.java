package com.sunghyun.football.global.exception.exceptions.noti;

import com.sunghyun.football.global.exception.ErrorCode;

public class MatchAlreadyDoneException extends NotiException{
    public MatchAlreadyDoneException(ErrorCode errorCode) {
        super(errorCode);
    }
}
