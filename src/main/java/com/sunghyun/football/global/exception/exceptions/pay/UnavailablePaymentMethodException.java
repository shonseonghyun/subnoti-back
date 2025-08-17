package com.sunghyun.football.global.exception.exceptions.pay;

import com.sunghyun.football.global.exception.ErrorCode;

public class UnavailablePaymentMethodException extends PayException{
    public UnavailablePaymentMethodException(ErrorCode errorCode) {
        super(errorCode);
    }
}
