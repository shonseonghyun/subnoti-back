package com.sunghyun.football.domain.noti.api;

import com.sunghyun.football.domain.noti.application.NotiApplication;
import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiReqDto;
import com.sunghyun.football.domain.stadium.enums.EnumMapper;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
public class NotiApi {
    private final EnumMapper enumSubTypesMapper;
    private final NotiApplication notiApplication;

    @PostMapping("/freeSub")
    public ApiResponseDto regFreeSubNoti(@RequestBody FreeSubNotiReqDto freeSubNotiReqDto){
        notiApplication.regFreeSubNoti(freeSubNotiReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @GetMapping("/freeSub/types")
    public ApiResponseDto getSubTypes(){
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,enumSubTypesMapper.getAll());
    }
}
