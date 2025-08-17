package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.ErrorCode;

public class SubscriptionAlreadyExistsException extends SubscriptionException {
    public SubscriptionAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
