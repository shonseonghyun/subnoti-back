package com.sunghyun.football.global.exception.pay.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.pay.PayException;

public class PayNotFoundException extends PayException {
    public PayNotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
