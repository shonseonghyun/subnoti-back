package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.ErrorCode;

public class SubscriptionExpiredException extends SubscriptionException{
    public SubscriptionExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
