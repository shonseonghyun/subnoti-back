package com.sunghyun.football.global.exception.noti;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class NotiException extends AppException {
    public NotiException(ErrorType errorType) {
        super(errorType);
    }
}
