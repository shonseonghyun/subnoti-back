package com.sunghyun.football.domain.history.api;

import com.sunghyun.football.domain.history.application.dto.SaveMailReqDto;
import com.sunghyun.football.domain.history.application.service.MailHistoryService;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/mail")
@RestController
@RequiredArgsConstructor
public class MailHistoryApi {

    private final MailHistoryService mailHistoryService;

    @PostMapping
    public ApiResponseDto saveMailHistory(@RequestBody @Valid final SaveMailReqDto saveMailReqDto){
        mailHistoryService.saveMailHistory(saveMailReqDto);
        return ApiResponseDto.toResponse(ErrorType.SUCCESS);
    }

}
