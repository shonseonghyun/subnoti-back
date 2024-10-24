package com.sunghyun.football.global.exception.exceptions.noti;

import com.sunghyun.football.global.exception.ErrorCode;

public class FreeSubNotiAlreadyRequestedException extends NotiException{
    public FreeSubNotiAlreadyRequestedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
