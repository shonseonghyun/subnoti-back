package com.sunghyun.football.config.batch.noti;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.FreeSubNotiHistory;
import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.noti.infrastructure.FreeSubNotiHistoryComparator;
import com.sunghyun.football.global.noti.notification.NotificationFacade;
import com.sunghyun.football.global.noti.message.build.dto.FreeSubNotiMessageDto;
import com.sunghyun.football.global.noti.type.NotiType;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotiProcessor {
    private final NotificationFacade notificationFacade;

    public void doNotiProcess(FreeSubNoti freeSubNoti, boolean managerFreeFlg, boolean superSubFlg) {
        ActiveType activeType = null;
        FreeSubType freeSubType = null;

        //historyNo 기준으로 내림차순으로 정렬
        log.info("historyNo 기준으로 내림차순으로 정렬");
        freeSubNoti.getFreeSubNotiHistories().sort(new FreeSubNotiHistoryComparator());


        //활성화 발송 조건 체크
        if(isActiveTurn(freeSubNoti.getFreeSubNotiHistories())){
            if(freeSubNoti.getSubType().equals(FreeSubType.MANAGER_FREE)){
                if(isManagerFreeActive(managerFreeFlg)) {
                    log.info("{}({}) 매니저 서브 활성화", freeSubNoti.getMatchName(), freeSubNoti.getMatchNo());
                    freeSubType = FreeSubType.MANAGER_FREE;
                }
            }else if(freeSubNoti.getSubType().equals(FreeSubType.SUPER_SUB)){
                if(isSuperSubActive(superSubFlg)){
                    log.info("{}({}) 슈퍼 서브 활성화", freeSubNoti.getMatchName(), freeSubNoti.getMatchNo());
                    freeSubType = FreeSubType.SUPER_SUB;
                }
            }
            activeType = ActiveType.ACTIVE;
        }

        //비활성화 발송 조건 체크
        else {
            if(isInActiveTurn(freeSubNoti.getFreeSubNotiHistories())){
                if(freeSubNoti.getSubType().equals(FreeSubType.MANAGER_FREE)){
                    if(!isManagerFreeActive(managerFreeFlg)){
                        log.info("{}({}) 매니저 서브 비활성화", freeSubNoti.getMatchName(), freeSubNoti.getMatchNo());
                        freeSubType = FreeSubType.MANAGER_FREE;
                    }
                }else if(freeSubNoti.getSubType().equals(FreeSubType.SUPER_SUB)){
                    if(!isSuperSubActive(superSubFlg)){
                        log.info("{}({}) 슈퍼 서브 비활성화", freeSubNoti.getMatchName(), freeSubNoti.getMatchNo());
                        freeSubType = FreeSubType.SUPER_SUB;
                    }
                }

                activeType = ActiveType.INACTIVE;
            }
        }

        //위 조건에 따라 설정된 플래그가 모두 세팅된 경우에만 플래그의 값에 따라 a)노티 발송 및 b)히스토리 엔티티 생성
        if(activeType != null && freeSubType != null){
            //a)노티 발송
            notificationFacade.notify(
                    NotiType.NOTI_FREE_SUB,
                    freeSubNoti.getEmail(),
                    new FreeSubNotiMessageDto(freeSubNoti.getMatchNo(), freeSubNoti.getMatchName(), freeSubNoti.getStartDt(), freeSubNoti.getStartTm(), freeSubType, activeType)
            );


            //b) 히스토리 엔티티 생성 및 추가
            freeSubNoti.getFreeSubNotiHistories().add(
                    FreeSubNotiHistory.builder()
                            .activeType(activeType)
                            .subType(freeSubType)
                            .sendDt(MatchDateUtils.getNowDtStr())
                            .sendTm(MatchDateUtils.getNowTmStr())
                            .build()
            );
        }
    }

    public boolean isActiveTurn(List<FreeSubNotiHistory> list){
        return list.isEmpty() || list.get(0).getActiveType().equals(ActiveType.INACTIVE);
    }

    public boolean isInActiveTurn(List<FreeSubNotiHistory> list){
        return list.get(0).getActiveType().equals(ActiveType.ACTIVE);
    }

    public boolean isManagerFreeActive(boolean managerFreeFlg){
        return managerFreeFlg;
    }

    public boolean isSuperSubActive(boolean superSubFlg){
        return superSubFlg;
    }
}
