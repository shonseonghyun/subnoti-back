package com.sunghyun.football.global.feign;

import com.sunghyun.football.config.FeignConfig;
import com.sunghyun.football.domain.match.application.dto.SelectMatchResDto;
import com.sunghyun.football.global.response.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "matchOpenFeignClient",url = "${service.url}" , configuration = FeignConfig.class)
public interface MatchOpenFeignClient {

    @GetMapping("/api/v1/match/{matchNo}")
    public ApiResponseDto<SelectMatchResDto> getMatch(@PathVariable("matchNo") final Long matchNo);

}
