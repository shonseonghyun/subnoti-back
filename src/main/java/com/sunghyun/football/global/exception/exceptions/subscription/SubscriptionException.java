package com.sunghyun.football.global.exception.exceptions.subscription;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class SubscriptionException extends AppException {
    public SubscriptionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
