package com.sunghyun.football.domain.noti.infrastructure.entity;

import com.sunghyun.football.domain.noti.domain.MatchFreeSubNoti;
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
public class MatchFreeSubNotiEntity {

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

    private String endTm;

    @Convert(converter = FreeSubTypeConverter.class)
    private FreeSubType subType;

    @OneToMany
    @JoinColumn(name = "notiNo")
    private List<FreeSubNotiHistoryEntity> freeSubNotiHistories;

    public static MatchFreeSubNotiEntity from(MatchFreeSubNoti freeSubNoti){
        MatchFreeSubNotiEntity matchFreeSubNotiEntity = new MatchFreeSubNotiEntity();
        matchFreeSubNotiEntity.notiNo=freeSubNoti.getNotiNo();
        matchFreeSubNotiEntity.email=freeSubNoti.getEmail();
        matchFreeSubNotiEntity.matchNo=freeSubNoti.getMatchNo();
//        matchFreeSubNotiEntity.sendFlg = freeSubNoti.getSendFlg();
        matchFreeSubNotiEntity.matchName= freeSubNoti.getMatchName();
        matchFreeSubNotiEntity.startDt=freeSubNoti.getStartDt();
        matchFreeSubNotiEntity.startTm=freeSubNoti.getStartTm();
        matchFreeSubNotiEntity.endTm=freeSubNoti.getEndTm();
        matchFreeSubNotiEntity.subType=freeSubNoti.getSubType();
        return matchFreeSubNotiEntity;
    }

    public MatchFreeSubNoti toModel(){
        return MatchFreeSubNoti.builder()
                .notiNo(notiNo)
                .matchNo(matchNo)
                .email(email)
                .matchName(matchName)
                .startDt(startDt)
                .startTm(startTm)
                .endTm(endTm)
                .subType(subType)
                .build();
    }
}
