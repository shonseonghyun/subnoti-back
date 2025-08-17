package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.ErrorCode;

public class SubscriptionNotFoundException extends SubscriptionException{
    public SubscriptionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
