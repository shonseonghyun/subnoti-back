package com.sunghyun.football.domain.noti.api;

import com.sunghyun.football.domain.noti.application.NotiApplication;
import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiRegReqDto;
import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiSelectResDto;
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
//    private final EnumMapper enumSubTypesMapper;
    private final NotiApplication notiApplication;

//    @GetMapping("/freeSub/types")
//    public ApiResponseDto getSubTypes(){
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,enumSubTypesMapper.getAll());
//    }

    @PostMapping("/freeSub")
    public ApiResponseDto regFreeSubNoti(@Valid @RequestBody final FreeSubNotiRegReqDto freeSubNotiRegReqDto){
        notiApplication.regFreeSubNoti(freeSubNotiRegReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @DeleteMapping("/freeSub/{notiNo}")
    public ApiResponseDto delFreeSubNoti(@PathVariable final Long notiNo){
        notiApplication.delFreeSubNoti(notiNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @RequestMapping("/freeSub/member/{memberNo}/dates")
    public ApiResponseDto getNotiRegDtsByDt(@PathVariable final Long memberNo,@RequestParam String startDt,@RequestParam String endDt){
        List<String> regDates = notiApplication.getNotiRegDtsByDt(memberNo,startDt,endDt);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,regDates);
    }

    @RequestMapping("/freeSub/member/{memberNo}/date/{selectedDt}")
    public ApiResponseDto getFreeSubNotiesByDate(
                                                @PathVariable final Long memberNo,
                                                @PathVariable final String selectedDt,
                                                @RequestParam(defaultValue = "2") Integer pageSize,
                                                @RequestParam(required = false) Long notiNo
    ){
        FreeSubNotiSelectResDto result = notiApplication.getFreeSubNotiesByDate(memberNo,selectedDt,notiNo,pageSize);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,result);
    }
}
