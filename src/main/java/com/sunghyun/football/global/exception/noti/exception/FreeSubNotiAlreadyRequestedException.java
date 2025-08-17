package com.sunghyun.football.global.exception.noti.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.noti.NotiException;

public class FreeSubNotiAlreadyRequestedException extends NotiException {
    public FreeSubNotiAlreadyRequestedException(ErrorType errorType) {
        super(errorType);
    }
}
