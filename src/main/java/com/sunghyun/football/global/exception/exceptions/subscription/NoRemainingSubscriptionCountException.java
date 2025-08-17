package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.ErrorCode;

public class NoRemainingSubscriptionCountException extends SubscriptionException{
    public NoRemainingSubscriptionCountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
