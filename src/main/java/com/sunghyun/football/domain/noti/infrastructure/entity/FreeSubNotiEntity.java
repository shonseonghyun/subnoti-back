package com.sunghyun.football.domain.noti.infrastructure.entity;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.converter.FreeSubTypeConverter;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "FreeSubNotiReq")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FreeSubNotiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notiNo;

    private Long matchNo;

    private String email;

//    @Convert(converter = SendFlgConverter.class)
//    private SendFlg sendFlg;

    private String matchName;

    private String startDt;

    private String startTm;

//    private String endTm;

    @Convert(converter = FreeSubTypeConverter.class)
    private FreeSubType subType;

    @OneToMany(cascade = {CascadeType.MERGE}
//            ,mappedBy = "matchFreeSubNotiEntity"
    )
    @JoinColumn(name = "notiNo")
    private List<FreeSubNotiHistoryEntity> freeSubNotiHistories;

    public static FreeSubNotiEntity from(FreeSubNoti freeSubNoti){
        FreeSubNotiEntity freeSubNotiEntity = new FreeSubNotiEntity();
        freeSubNotiEntity.notiNo=freeSubNoti.getNotiNo();
        freeSubNotiEntity.email=freeSubNoti.getEmail();
        freeSubNotiEntity.matchNo=freeSubNoti.getMatchNo();
//        freeSubNotiEntity.sendFlg = freeSubNoti.getSendFlg();
        freeSubNotiEntity.matchName= freeSubNoti.getMatchName();
        freeSubNotiEntity.startDt=freeSubNoti.getStartDt();
        freeSubNotiEntity.startTm=freeSubNoti.getStartTm();
//        freeSubNotiEntity.endTm=freeSubNoti.getEndTm();
        freeSubNotiEntity.subType=freeSubNoti.getSubType();
        return freeSubNotiEntity;
    }

    public FreeSubNoti toModel(){
        return FreeSubNoti.builder()
                .notiNo(notiNo)
                .matchNo(matchNo)
                .email(email)
                .matchName(matchName)
                .startDt(startDt)
                .startTm(startTm)
//                .endTm(endTm)
                .subType(subType)
                .build();
    }
}
