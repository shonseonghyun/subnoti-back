package com.sunghyun.football.config;

import feign.Client;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }
}
