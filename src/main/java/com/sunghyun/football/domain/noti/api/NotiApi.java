package com.sunghyun.football.domain.noti.api;

import com.sunghyun.football.domain.noti.application.NotiApplication;
import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiRegReqDto;
import com.sunghyun.football.domain.noti.application.dto.SelectFreeSubNotiResDto;
import com.sunghyun.football.domain.stadium.enums.EnumMapper;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
public class NotiApi {
    private final EnumMapper enumSubTypesMapper;
    private final NotiApplication notiApplication;

    @GetMapping("/freeSub/types")
    public ApiResponseDto getSubTypes(){
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,enumSubTypesMapper.getAll());
    }

    @PostMapping("/freeSub")
    public ApiResponseDto regFreeSubNoti(@Valid @RequestBody FreeSubNotiRegReqDto freeSubNotiRegReqDto){
        notiApplication.regFreeSubNoti(freeSubNotiRegReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @GetMapping("/freeSub/member/{memberNo}")
    public ApiResponseDto getFreeSubNoties(@Valid @PathVariable Long memberNo){
//      public ApiResponseDto getFreeSubNoties(@Valid @RequestBody FreeSubNotiSelectReqDto freeSubNotiSelectReqDto){
//        List<SelectFreeSubNotiResDto> result = notiApplication.getFreeSubNoties(freeSubNotiSelectReqDto.getMemberNo());
        List<SelectFreeSubNotiResDto> result = notiApplication.getFreeSubNoties(memberNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,result);
    }
}
