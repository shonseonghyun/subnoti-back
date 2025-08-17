package com.sunghyun.football.global.exception.noti.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.noti.NotiException;

public class FreesubTypeNotFoundException extends NotiException {
    public FreesubTypeNotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
