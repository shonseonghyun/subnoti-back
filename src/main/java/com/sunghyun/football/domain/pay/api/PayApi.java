package com.sunghyun.football.domain.pay.api;

import com.sunghyun.football.domain.pay.application.PayService;
import com.sunghyun.football.domain.pay.application.dto.PayReqDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pay")
@RequiredArgsConstructor
public class PayApi {

    private final PayService payService;

    @PostMapping("")
    public ApiResponseDto pay(@RequestBody final PayReqDto payReqDto){
        payService.pay(payReqDto.getMemberNo(),payReqDto.getAmount(),payReqDto.getPaymentMethod());
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }
}
