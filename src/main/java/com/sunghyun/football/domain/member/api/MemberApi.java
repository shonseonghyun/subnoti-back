package com.sunghyun.football.domain.member.api;

import com.sunghyun.football.domain.member.application.JoinService;
import com.sunghyun.football.domain.member.application.MemberApplication;
import com.sunghyun.football.domain.member.application.dto.MemberJoinReqDto;
import com.sunghyun.football.domain.member.application.dto.MemberJoinResDto;
import com.sunghyun.football.domain.member.application.dto.SelectMemberResDto;
import com.sunghyun.football.domain.member.domain.dto.MemberUpdReqDto;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return ApiResponseDto.toResponse(ErrorType.SUCCESS,selectMemberResDto);
    }

    @PutMapping("/{memberNo}")
    public ApiResponseDto update(@PathVariable("memberNo") final Long memberNo,@RequestBody @Valid final MemberUpdReqDto memberUpdReqDto){
        memberApplication.updateMember(memberNo,memberUpdReqDto);
        return ApiResponseDto.toResponse(ErrorType.SUCCESS);
    }

    @DeleteMapping("/{memberNo}")
    public ApiResponseDto delete(@PathVariable("memberNo") Long memberNo){
        memberApplication.deleteMember(memberNo);
        return ApiResponseDto.toResponse(ErrorType.SUCCESS);
    }

    @PostMapping("")
    public ApiResponseDto join(@RequestBody @Valid MemberJoinReqDto memberJoinReqDto){
        MemberJoinResDto memberJoinResDto = joinService.join(memberJoinReqDto);
        return ApiResponseDto.toResponse(ErrorType.SUCCESS,memberJoinResDto);
    }

    @GetMapping("/email/duplicate/{email}")
    public ApiResponseDto checkEmailDuplication(@PathVariable("email") String email){
        boolean flg = joinService.checkEmailDuplication(email);
        return ApiResponseDto.toResponse(ErrorType.SUCCESS,flg);
    }

}
