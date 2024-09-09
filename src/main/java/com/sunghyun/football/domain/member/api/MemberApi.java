package com.sunghyun.football.domain.member.api;

import com.sunghyun.football.domain.member.application.JoinService;
import com.sunghyun.football.domain.member.application.MemberApplication;
import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberApplication memberApplication;
    private final JoinService joinService;

    @GetMapping("/{memberNo}")
    public ApiResponseDto getMember(@PathVariable("memberNo") final Long memberNo){
        SelectMemberResDto selectMemberResDto = memberApplication.getMember(memberNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,selectMemberResDto);
    }

    @PutMapping("/{memberNo}")
    public ApiResponseDto update(@RequestBody MemberUpdReqDto memberUpdReqDto){
        memberApplication.updateMember(memberUpdReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @DeleteMapping("/{memberNo}")
    public ApiResponseDto delete(@PathVariable("memberNo") Long memberNo){
        memberApplication.deleteMember(memberNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @PostMapping("")
    public ApiResponseDto join(@RequestBody @Valid MemberJoinReqDto memberJoinReqDto){
        MemberJoinResDto memberJoinResDto = joinService.join(memberJoinReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,memberJoinResDto);
    }

    @GetMapping("/email/duplicate/{email}")
    public ApiResponseDto checkEmailDuplication(@PathVariable("email") String email){
        boolean flg = joinService.checkEmailDuplication(email);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,flg);
    }

}
