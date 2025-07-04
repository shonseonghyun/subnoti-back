//package com.sunghyun.football.domain.match.api;
//
//import com.sunghyun.football.domain.match.application.MatchApplication;
//import com.sunghyun.football.domain.match.application.MatchNamedLockFacade;
//import com.sunghyun.football.domain.match.application.MatchOptimisticLockFacade;
//import com.sunghyun.football.domain.match.application.ViewCountService;
//import com.sunghyun.football.domain.match.application.dto.RegMatchReqDto;
//import com.sunghyun.football.domain.match.application.dto.SelectMatchResDto;
//import com.sunghyun.football.domain.match.application.dto.SelectSimpleMatchResDto;
//import com.sunghyun.football.domain.match.domain.dto.SearchMatchesReqDto;
//import com.sunghyun.football.global.enums.EnumMapper;
//import com.sunghyun.football.global.exception.ErrorCode;
//import com.sunghyun.football.global.response.ApiResponseDto;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1")
//@RequiredArgsConstructor
//public class MatchApi {
//
//    private final MatchApplication matchApplication;
//    private final ViewCountService viewCountService;
//    private final MatchOptimisticLockFacade matchOptimisticLockFacade;
//    private final MatchNamedLockFacade matchNamedLockFacade;
//    private final EnumMapper enumRuleMapper;
//
//    @GetMapping("/matches")
//    public ApiResponseDto getMatches(@ModelAttribute @Valid SearchMatchesReqDto searchMatchesReqDto){
//        List<SelectSimpleMatchResDto> selectSimpleMatchResDtos = matchApplication.getMatchesByConditions(searchMatchesReqDto);
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,selectSimpleMatchResDtos);
//    }
//
//    @GetMapping("/match/rules")
//    public ApiResponseDto getRules(){
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,enumRuleMapper.getAll());
//    }
//
//    @GetMapping("/match/{matchNo}")
//    public ApiResponseDto getMatch(
//            @PathVariable("matchNo") Long matchNo,
//            @RequestBody(required = false) Map<String,Long> map
//    ) {
//        //body 내 아무것도 안 담겨져 올 경우 직접 EmptyMap 세팅
//        Map<String,Long> checkedMap = map == null ? Collections.EMPTY_MAP : map;
//
//        final Long memberNo = checkedMap.get("memberNo");
//        final String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
//
//        viewCountService.addViewCountProcess(matchNo,memberNo,ipAddress);
//        SelectMatchResDto selectMatchResDto = matchNamedLockFacade.getMatch(matchNo);
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS,selectMatchResDto);
//    }
//
//    @PostMapping("/match")
//    public ApiResponseDto regMatch(@RequestBody @Valid RegMatchReqDto regMatchReqDto){
//        Long matchNo = matchApplication.regMatch(regMatchReqDto);
//        viewCountService.regMatch(matchNo);
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
//    }
//
//    @PutMapping("/match/{matchNo}/manager/{memberNo}")
//    public ApiResponseDto regManagerInMatch(@PathVariable("matchNo") Long matchNo, @PathVariable("memberNo") Long memberNo){
//        matchApplication.regManagerInMatch(matchNo,memberNo);
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
//    }
//
//    @PutMapping("/match/{matchNo}/apply/member/{memberNo}")
//    public ApiResponseDto receivePlayer(@PathVariable("matchNo") Long matchNo, @PathVariable("memberNo") Long memberNo){
//        matchApplication.receivePlayer(matchNo,memberNo);
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
//    }
//
//    @PutMapping("/match/{matchNo}/cancel/member/{memberNo}")
//    public ApiResponseDto cancelPlayer(@PathVariable("matchNo") Long matchNo, @PathVariable("memberNo") Long memberNo){
//        matchApplication.cancelPlayer(matchNo,memberNo);
//        return ApiResponseDto.toResponse(ErrorCode.SUCCESS);
//    }
//
//}
