package com.sunghyun.football.domain.noti.application;

import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiReqDto;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.noti.FreeSubNotiAlreadyRequestedException;
import com.sunghyun.football.global.exception.exceptions.noti.MatchAlreadyDoneException;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotiApplication {
    private final FreeSubNotiRepository freeSubNotiRepository;
    private final PlabFootBallOpenFeignClient plabFootBallOpenFeignClient;

    //등록
    public void regFreeSubNoti(FreeSubNotiReqDto freeSubNotiReqDto){
        //이미 신청한 매치인지 확인
        freeSubNotiRepository.getFreeSubTypes(freeSubNotiReqDto.getEmail(),freeSubNotiReqDto.getMatchNo()).stream()
                .filter(item->item.equals(freeSubNotiReqDto.getSubType()))
                .findFirst()
                .ifPresent(item->{throw new FreeSubNotiAlreadyRequestedException(ErrorCode.FREESUB_NOTI_ALREADY_REQUEST);}
                );

        //플랩 api 통신
        PlabMatchInfoResDto response= plabFootBallOpenFeignClient.getMatch(freeSubNotiReqDto.getMatchNo());

        //유효성 검증 처리
        if(MatchDateUtils.hasAlreadyPassedOfMatch(MatchDateUtils.getDtStr(response.getSchedule()),MatchDateUtils.getTmStr(response.getSchedule()))){
            throw new MatchAlreadyDoneException(ErrorCode.MATCH_ALREADY_DONE);
        }

        //도메인 생성
        FreeSubNoti freeSubNoti = FreeSubNoti.builder()
                .email(freeSubNotiReqDto.getEmail())
                .matchNo(freeSubNotiReqDto.getMatchNo())
//                .sendFlg(SendFlg.NOT_SEND)
                .matchName(response.getLabel_title())
                .startDt(MatchDateUtils.getDtStr(response.getSchedule()))
                .startTm(MatchDateUtils.getTmStr(response.getSchedule()))
//                .endTm(MatchDateUtils.getTmAfterHours(response.getSchedule(),2))
                .subType(freeSubNotiReqDto.getSubType())
                .build();

        //DB저장
        freeSubNotiRepository.save(freeSubNoti);
    }
}
