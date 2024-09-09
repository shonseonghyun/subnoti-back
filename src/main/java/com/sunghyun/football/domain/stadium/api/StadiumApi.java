package com.sunghyun.football.domain.stadium.api;

import com.sunghyun.football.domain.stadium.application.StadiumApplication;
import com.sunghyun.football.domain.stadium.application.dto.RegStadiumReqDto;
import com.sunghyun.football.domain.stadium.application.dto.SelectStadiumResDto;
import com.sunghyun.football.domain.stadium.application.dto.UpdateStadiumReqDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stadium")
public class StadiumApi {

    private final StadiumApplication stadiumApplication;

    @GetMapping("/{stadiumNo}")
    public ApiResponseDto getStadium(@PathVariable("stadiumNo") final Long stadiumNo){
        SelectStadiumResDto selectStadiumResDto = stadiumApplication.getStadium(stadiumNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,selectStadiumResDto);
    }

    @PostMapping
    public ApiResponseDto regStadium(@RequestPart @Valid final RegStadiumReqDto regStadiumReqDto
            , @RequestPart(value = "files") final List<MultipartFile> stadiumImageFiles
    ){
        stadiumApplication.regStadium(regStadiumReqDto,stadiumImageFiles);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }
    @PutMapping("/{stadiumNo}")
    public ApiResponseDto updateStadium(@PathVariable("stadiumNo") final Long stadiumNo,
                              @RequestBody final @Valid UpdateStadiumReqDto updateStadiumReqDto){
        stadiumApplication.updateStadium(stadiumNo,updateStadiumReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @DeleteMapping("/{stadiumNo}")
    public ApiResponseDto deleteStadium(@PathVariable("stadiumNo") final Long stadiumNo){
        stadiumApplication.deleteStadium(stadiumNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }
}
