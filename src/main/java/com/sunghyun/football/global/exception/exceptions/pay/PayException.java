package com.sunghyun.football.global.exception.exceptions.pay;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class PayException extends AppException {
    public PayException(ErrorCode errorCode) {
        super(errorCode);
    }
}
