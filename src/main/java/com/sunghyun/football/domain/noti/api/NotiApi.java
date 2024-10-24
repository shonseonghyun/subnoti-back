package com.sunghyun.football.domain.noti.api;

import com.sunghyun.football.domain.noti.application.NotiApplication;
import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiReqDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
public class NotiApi {

    private final NotiApplication notiApplication;

    @PostMapping("/freeSub")
    public ApiResponseDto regFreeSubNoti(@RequestBody FreeSubNotiReqDto freeSubNotiReqDto){
        notiApplication.regFreeSubNoti(freeSubNotiReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }
}
