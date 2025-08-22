package com.sunghyun.football.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.enumMapper.enums.EnumMapper;
import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.pay.domain.model.PaymentMethod;
import com.sunghyun.football.domain.subscription.domain.model.SubscriptionPlan;
import com.sunghyun.football.global.exception.ErrorType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
public class AppConfig {

    @Bean
    public EnumMapper enumRuleMapper(){
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("subType", FreeSubType.class);
        enumMapper.put("gender", Gender.class);
        enumMapper.put("errorCode", ErrorType.class);
        enumMapper.put("subscriptionPlan", SubscriptionPlan.class);
        enumMapper.put("paymentMethod", PaymentMethod.class);
        return enumMapper;
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
