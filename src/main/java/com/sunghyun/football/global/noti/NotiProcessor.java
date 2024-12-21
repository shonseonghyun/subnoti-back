package com.sunghyun.football.global.noti;

import com.sunghyun.football.config.batch.NotiContentMaker;
import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.noti.infrastructure.FreeSubNotiHistoryComparator;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiHistoryEntity;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public abstract class NotiProcessor {

    protected abstract void doNoti(NotiSendReqDto notiSendReqDto);

    public void doNotiProcess(FreeSubNotiEntity freeSubNotiEntity, boolean managerFreeFlg, boolean superSubFlg){
        ActiveType activeType=null;
        FreeSubType freeSubType=null;

        //historyNo 기준으로 내림차순으로 정렬
        freeSubNotiEntity.getFreeSubNotiHistories().sort(new FreeSubNotiHistoryComparator());


        //활성화 발송 조건 체크
        if(isActiveTurn(freeSubNotiEntity.getFreeSubNotiHistories())){
            if(freeSubNotiEntity.getSubType().equals(FreeSubType.MANAGER_FREE)) {
                if(isManagerFreeActive(managerFreeFlg)){
                    log.info("{}({}) 매니저 서브 활성화",freeSubNotiEntity.getMatchName(),freeSubNotiEntity.getMatchNo());
                    freeSubType = FreeSubType.MANAGER_FREE;
                }
            }
            else if(freeSubNotiEntity.getSubType().equals(FreeSubType.SUPER_SUB)) {
                if (isSuperSubActive(superSubFlg)) {
                    log.info("{}({}) 슈퍼 서브 활성화", freeSubNotiEntity.getMatchName(), freeSubNotiEntity.getMatchNo());
                    freeSubType = FreeSubType.SUPER_SUB;
                }
            }
            activeType = ActiveType.ACTIVE;
        }

        //비활성화 발송 조건 체크
        else{
            if(isInActiveTurn(freeSubNotiEntity.getFreeSubNotiHistories())){
                if(freeSubNotiEntity.getSubType().equals(FreeSubType.MANAGER_FREE)){
                    if(!isManagerFreeActive(managerFreeFlg)){
                        log.info("{}({}) 매니저 서브 비활성화",freeSubNotiEntity.getMatchName(),freeSubNotiEntity.getMatchNo());
                        freeSubType = FreeSubType.MANAGER_FREE;
                    }
                }
                else if(freeSubNotiEntity.getSubType().equals(FreeSubType.SUPER_SUB)) {
                    if(!isSuperSubActive(superSubFlg)) {
                        log.info("{}({}) 슈퍼 서브 비활성화",freeSubNotiEntity.getMatchName(),freeSubNotiEntity.getMatchNo());
                        freeSubType = FreeSubType.SUPER_SUB;
                    }
                }

                activeType = ActiveType.INACTIVE;
            }
        }

        //위 조건에 따라 설정된 플래그가 모두 세팅된 경우에만 플래그의 값에 따라 a)노티 발송 및 b)히스토리 엔티티 생성
        if(activeType!=null && freeSubType!=null){
            //a)노티 발송
            this.doNoti(
                    NotiSendReqDto.builder()
                            .email(freeSubNotiEntity.getEmail())
                            .subject(NotiContentMaker.FreeSub.getSubject(freeSubNotiEntity,freeSubType,activeType))
                            .content(NotiContentMaker.FreeSub.getContent(freeSubNotiEntity,freeSubType,activeType))
                            .build()
            );

            //b) 히스토리 엔티티 생성 및 추가
            freeSubNotiEntity.getFreeSubNotiHistories().add(
                    FreeSubNotiHistoryEntity.builder()
                                .activeType(activeType)
                                .subType(freeSubType)
                                .sendDt(MatchDateUtils.getNowDtStr())
                                .sendTm(MatchDateUtils.getNowTmStr())
                                .build()
            );
        }
    }

    public boolean isActiveTurn(List<FreeSubNotiHistoryEntity> list){
        return list.isEmpty() || list.get(0).getActiveType().equals(ActiveType.INACTIVE);
    }

    public boolean isInActiveTurn(List<FreeSubNotiHistoryEntity> list){
        return list.get(0).getActiveType().equals(ActiveType.ACTIVE);
    }

    public boolean isManagerFreeActive(boolean managerFreeFlg){
        return managerFreeFlg;
    }

    public boolean isSuperSubActive(boolean superSubFlg){
        return superSubFlg;
    }
}
