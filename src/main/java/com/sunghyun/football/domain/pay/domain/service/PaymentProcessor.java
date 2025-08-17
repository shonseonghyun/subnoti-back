package com.sunghyun.football.domain.pay.domain.service;

import com.sunghyun.football.domain.pay.domain.model.Pay;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;

public interface PaymentProcessor{
    void pay();
    PaymentMethod getPaymentMethod();
    void refund(final Pay pay);
}
