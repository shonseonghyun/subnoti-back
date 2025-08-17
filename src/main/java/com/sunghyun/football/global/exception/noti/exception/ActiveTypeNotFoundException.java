package com.sunghyun.football.global.exception.noti.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.noti.NotiException;

public class ActiveTypeNotFoundException extends NotiException {
    public ActiveTypeNotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
