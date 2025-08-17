package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.ErrorCode;

public class SubscriptionAlreadyUsedException extends SubscriptionException{
    public SubscriptionAlreadyUsedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
