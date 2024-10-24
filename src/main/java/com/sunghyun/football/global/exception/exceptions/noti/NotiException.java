package com.sunghyun.football.global.exception.exceptions.noti;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class NotiException extends AppException {
    public NotiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
