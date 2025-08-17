package com.sunghyun.football.global.exception.subscription.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;

public class NoRemainingSubscriptionCountException extends SubscriptionException {
    public NoRemainingSubscriptionCountException(ErrorType errorType) {
        super(errorType);
    }
}
