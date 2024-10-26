package com.sunghyun.football.domain.noti.domain;

import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class FreeSubNotiHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    private String ActiveType;

    private FreeSubType subType;

    private String sendDt;

    private String sendTm;}
