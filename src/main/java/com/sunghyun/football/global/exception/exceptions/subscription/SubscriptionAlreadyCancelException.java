package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.ErrorCode;

public class SubscriptionAlreadyCancelException extends SubscriptionException{
    public SubscriptionAlreadyCancelException(ErrorCode errorCode) {
        super(errorCode);
    }
}
