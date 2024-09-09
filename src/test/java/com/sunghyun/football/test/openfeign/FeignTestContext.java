package com.sunghyun.football.test.openfeign;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@ImportAutoConfiguration({
//        OpenFeignConfig.class,
//        CustomPropertiesConfig.class,
        FeignAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
})
public class FeignTestContext {
}
