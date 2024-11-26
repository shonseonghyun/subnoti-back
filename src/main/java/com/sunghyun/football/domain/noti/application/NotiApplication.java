package com.sunghyun.football.domain.noti.application;

import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiRegReqDto;
import com.sunghyun.football.domain.noti.application.dto.SelectFreeSubNotiResDto;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.noti.FreeSubNotiAlreadyRequestedException;
import com.sunghyun.football.global.exception.exceptions.noti.MatchAlreadyDoneException;
import com.sunghyun.football.global.feign.MemberOpenFeignClient;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotiApplication {
    private final FreeSubNotiRepository freeSubNotiRepository;
    private final PlabFootBallOpenFeignClient plabFootBallOpenFeignClient;
    private final MemberOpenFeignClient memberOpenFeignClient;

    //등록
    public void regFreeSubNoti(FreeSubNotiRegReqDto freeSubNotiRegReqDto){
        //이미 신청한 매치인지 확인
        freeSubNotiRepository.getFreeSubTypes(freeSubNotiRegReqDto.getEmail(), freeSubNotiRegReqDto.getMatchNo()).stream()
                .filter(item->item.equals(freeSubNotiRegReqDto.getSubType()))
                .findFirst()
                .ifPresent(item->{throw new FreeSubNotiAlreadyRequestedException(ErrorCode.FREESUB_NOTI_ALREADY_REQUEST);}
                );

        //플랩 api 통신
        PlabMatchInfoResDto response= plabFootBallOpenFeignClient.getMatch(freeSubNotiRegReqDto.getMatchNo());

        //유효성 검증 처리
        if(MatchDateUtils.hasAlreadyPassedOfMatch(MatchDateUtils.getDtStr(response.getSchedule()),MatchDateUtils.getTmStr(response.getSchedule()))){
            throw new MatchAlreadyDoneException(ErrorCode.MATCH_ALREADY_DONE);
        }

        //도메인 생성
        FreeSubNoti freeSubNoti = FreeSubNoti.builder()
                .memberNo(freeSubNotiRegReqDto.getMemberNo())
                .email(freeSubNotiRegReqDto.getEmail())
                .matchNo(freeSubNotiRegReqDto.getMatchNo())
                .matchName(response.getLabel_title())
                .startDt(MatchDateUtils.getDtStr(response.getSchedule()))
                .startTm(MatchDateUtils.getTmStr(response.getSchedule()))
                .subType(freeSubNotiRegReqDto.getSubType())
                .build();

        //DB저장
        freeSubNotiRepository.save(freeSubNoti);
    }

    public List<SelectFreeSubNotiResDto> getFreeSubNoties(Long memberNo) {
        return freeSubNotiRepository.getFreeSubNoties(memberNo)
                .stream()
                .map(SelectFreeSubNotiResDto::toDto)
                .toList()
                ;
    }

    public void delFreeSubNoti(Long notiNo) {
        freeSubNotiRepository.delFreeSubNoti(notiNo);
    }
}
