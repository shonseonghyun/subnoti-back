package com.sunghyun.football.global.exception.exceptions.pay;

import com.sunghyun.football.global.exception.ErrorCode;

public class UnsupportedPaymentMethodException extends PayException{
    public UnsupportedPaymentMethodException(ErrorCode errorCode) {
        super(errorCode);
    }
}
