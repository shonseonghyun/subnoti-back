package com.sunghyun.football.global.feign;

import com.sunghyun.football.config.FeignConfig;
import com.sunghyun.football.domain.stadium.application.dto.SelectStadiumResDto;
import com.sunghyun.football.global.response.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stadiumOpenFeignClient" , url = "http://localhost:8000",configuration = FeignConfig.class)
public interface StadiumOpenFeignClient {

    @GetMapping("/api/v1/stadium/{stadiumNo}")
    public ApiResponseDto<SelectStadiumResDto> checkExistStadium(@PathVariable("stadiumNo") Long stadiumNo);
}
