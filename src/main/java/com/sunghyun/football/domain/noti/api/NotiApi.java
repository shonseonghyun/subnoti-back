package com.sunghyun.football.domain.noti.api;

import com.sunghyun.football.domain.noti.application.NotiApplication;
import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiRegReqDto;
import com.sunghyun.football.domain.noti.application.dto.SelectFreeSubNotiResDto;
import com.sunghyun.football.domain.stadium.enums.EnumMapper;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import com.sunghyun.football.global.utils.MatchDateUtils;
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
    public ApiResponseDto regFreeSubNoti(@Valid @RequestBody final FreeSubNotiRegReqDto freeSubNotiRegReqDto){
        notiApplication.regFreeSubNoti(freeSubNotiRegReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @GetMapping("/freeSub/member/{memberNo}")
    public ApiResponseDto getFreeSubNoties(@Valid @PathVariable final Long memberNo){
//      public ApiResponseDto getFreeSubNoties(@Valid @RequestBody FreeSubNotiSelectReqDto freeSubNotiSelectReqDto){
//        List<SelectFreeSubNotiResDto> result = notiApplication.getFreeSubNoties(freeSubNotiSelectReqDto.getMemberNo());
        final String nowDt = MatchDateUtils.getNowDtStr();
        List<SelectFreeSubNotiResDto> result = notiApplication.getFreeSubNoties(memberNo,nowDt);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,result);
    }

    @DeleteMapping("/freeSub/{notiNo}")
    public ApiResponseDto delFreeSubNoti(@PathVariable final Long notiNo){
        notiApplication.delFreeSubNoti(notiNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

}
