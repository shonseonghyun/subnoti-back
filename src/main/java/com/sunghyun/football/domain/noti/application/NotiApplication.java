package com.sunghyun.football.domain.noti.application;

import com.sunghyun.football.domain.noti.application.dto.FreeSubNotiReqDto;
import com.sunghyun.football.domain.noti.domain.MatchFreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.SendFlg;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.noti.FreeSubNotiAlreadyRequestedException;
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
        freeSubNotiRepository.getFreeSubNoti(freeSubNotiReqDto.getEmail(),freeSubNotiReqDto.getMatchNo())
                .ifPresent(entity-> {
                    throw new FreeSubNotiAlreadyRequestedException(ErrorCode.FREESUB_NOTI_ALREADY_REQUEST);
                });

        //플랩 api 통신
        PlabMatchInfoResDto response= plabFootBallOpenFeignClient.getMatch(freeSubNotiReqDto.getMatchNo());

        //도메인 생성
        MatchFreeSubNoti freeSubNoti = MatchFreeSubNoti.builder()
                .email(freeSubNotiReqDto.getEmail())
                .matchNo(freeSubNotiReqDto.getMatchNo())
                .sendFlg(SendFlg.NOT_SEND)
                .matchName(response.getLabel_title())
                .startDt(MatchDateUtils.getDtStr(response.getSchedule()))
                .startTm(MatchDateUtils.getTmStr(response.getSchedule()))
                .endTm(MatchDateUtils.getTmAfterHours(response.getSchedule(),2))
                .build();

        //DB저장
        freeSubNotiRepository.save(freeSubNoti);
    }
}
