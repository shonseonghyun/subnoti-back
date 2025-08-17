package com.sunghyun.football.global.exception.noti.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.noti.NotiException;

public class MatchAlreadyDoneException extends NotiException {
    public MatchAlreadyDoneException(ErrorType errorType) {
        super(errorType);
    }
}
