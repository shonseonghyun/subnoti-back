package com.sunghyun.football.global.feign;

import com.sunghyun.football.config.FeignConfig;
import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.global.response.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="memberOpenFeignClient", url = "${service.url}", configuration = FeignConfig.class)
public interface MemberOpenFeignClient {

    @GetMapping("/api/v1/member/{memberNo}")
    public ApiResponseDto<SelectMemberResDto> checkExistMember(@PathVariable("memberNo") Long member);
}
