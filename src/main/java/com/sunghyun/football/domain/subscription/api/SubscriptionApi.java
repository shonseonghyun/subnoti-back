package com.sunghyun.football.domain.subscription.api;

import com.sunghyun.football.domain.subscription.application.service.SubscriptionService;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionApi {
    private final SubscriptionService subscriptionService;

    @DeleteMapping("/{memberNo}/{subscriptionNo}")
    public ApiResponseDto cancel(
            @PathVariable("memberNo") final Long memberNo,
            @PathVariable("subscriptionNo") final Long subscriptionNo
    )
    {
        subscriptionService.cancel(memberNo,subscriptionNo, MatchDateUtils.getNowDtStr());
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }
}
