package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.pay.PayException;

public class InvalidSubscriptionAmountException extends PayException {
    public InvalidSubscriptionAmountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
