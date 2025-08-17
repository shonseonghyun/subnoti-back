package com.sunghyun.football.domain.enumMapper;

import com.sunghyun.football.domain.enumMapper.enums.EnumMapper;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/enum")
@RequiredArgsConstructor
public class EnumApi {

    private final EnumMapper enumRuleMapper;

    @GetMapping("/{key}")
    public ApiResponseDto getEnum(@PathVariable String key){
        return ApiResponseDto.toResponse(ErrorType.SUCCESS,enumRuleMapper.get(key));
    }

}
