package com.sunghyun.football.global.feign;

import com.sunghyun.football.config.FeignConfig;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "plabFootBallOpenFeignClient",url = "https://www.plabfootball.com" , configuration = FeignConfig.class)
public interface PlabFootBallOpenFeignClient {

    @GetMapping("/api/v2/matches/{matchNo}")
    public PlabMatchInfoResDto getMatch(@PathVariable("matchNo") final Long matchNo);

}
