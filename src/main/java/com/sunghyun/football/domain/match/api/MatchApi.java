package com.sunghyun.football.domain.match.api;

import com.sunghyun.football.domain.match.application.MatchApplication;
import com.sunghyun.football.domain.match.application.MatchNamedLockFacade;
import com.sunghyun.football.domain.match.application.MatchOptimisticLockFacade;
import com.sunghyun.football.domain.match.application.dto.RegMatchReqDto;
import com.sunghyun.football.domain.match.application.dto.SelectMatchResDto;
import com.sunghyun.football.domain.match.application.dto.SelectSimpleMatchResDto;
import com.sunghyun.football.domain.match.domain.dto.SearchMatchesReqDto;
import com.sunghyun.football.domain.stadium.enums.EnumMapper;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MatchApi {

    private final MatchApplication matchApplication;
    private final MatchOptimisticLockFacade matchOptimisticLockFacade;
    private final MatchNamedLockFacade matchNamedLockFacade;
    private final EnumMapper enumMapper;

    @GetMapping("/matches")
    public ApiResponseDto getMatches(@ModelAttribute @Valid SearchMatchesReqDto searchMatchesReqDto){
        List<SelectSimpleMatchResDto> selectSimpleMatchResDtos = matchApplication.getMatchesByConditions(searchMatchesReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,selectSimpleMatchResDtos);
    }

    @GetMapping("/match/rules")
    public ApiResponseDto getRules(){
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,enumMapper.getAll());
    }

    @GetMapping("/match/{matchNo}")
    public ApiResponseDto getMatch(@PathVariable("matchNo") Long matchNo) throws InterruptedException {
        SelectMatchResDto selectMatchResDto = matchNamedLockFacade.getMatch(matchNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,selectMatchResDto);
    }

    @GetMapping("/match/view/{matchNo}")
    public ApiResponseDto getMatchView(@PathVariable("matchNo") Long matchNo){
        matchApplication.clickedMatch(matchNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @PostMapping("/match")
    public ApiResponseDto regMatch(@RequestBody @Valid RegMatchReqDto regMatchReqDto){
        matchApplication.regMatch(regMatchReqDto);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @PutMapping("/match/{matchNo}/manager/{memberNo}")
    public ApiResponseDto regManagerInMatch(@PathVariable("matchNo") Long matchNo, @PathVariable("memberNo") Long memberNo){
        matchApplication.regManagerInMatch(matchNo,memberNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @PutMapping("/match/{matchNo}/apply/member/{memberNo}")
    public ApiResponseDto receivePlayer(@PathVariable("matchNo") Long matchNo, @PathVariable("memberNo") Long memberNo){
        matchApplication.receivePlayer(matchNo,memberNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

    @PutMapping("/match/{matchNo}/cancel/member/{memberNo}")
    public ApiResponseDto cancelPlayer(@PathVariable("matchNo") Long matchNo, @PathVariable("memberNo") Long memberNo){
        matchApplication.cancelPlayer(matchNo,memberNo);
        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
    }

}
