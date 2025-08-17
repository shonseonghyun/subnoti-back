package com.sunghyun.football.global.exception.exceptions.pay;

import com.sunghyun.football.global.exception.ErrorCode;

public class PayNotFoundException extends PayException{
    public PayNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
