package com.sunghyun.football.global.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class MatchDateUtils {
    public static String getNowDtStr(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date());
    }

    public static String getNowTmStr(){
        DateFormat tmFormat = new SimpleDateFormat("HHmmss");
        return tmFormat.format(new Date());
    }


    public static String getDtStr(Date date){
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        return dtFormat.format(date);
    }

    public static String getTmStr(Date date){
        SimpleDateFormat tmFormat = new SimpleDateFormat("HHmmSS");
        return tmFormat.format(date);
    }

    public static String getTmAfterHours(Date date,int hours){
        SimpleDateFormat tmFormat = new SimpleDateFormat("HHmm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,hours);

        return tmFormat.format(calendar.getTime());
    }

    public static boolean hasAlreadyPassedOfMatch(String startDt,String startTm){
        final String nowDt= MatchDateUtils.getNowDtStr();
        final String nowTm = MatchDateUtils.getNowTmStr();

        boolean isStartOrEndFlg = false;

        if(startDt.compareTo(nowDt)<0){
//            log.info("이미 종료된 매치이므로 제외 - 매치 시작 일자[{}]/현재 일자[{}]",startDt,nowDt);
            isStartOrEndFlg=true;
        }
        else if(startDt.compareTo(nowDt)==0){
            if(startTm.compareTo(nowTm)<=0){
//                log.info("이미 시작된 매치이므로 제외 - 매치 시작 시간[{}]/현재 시간[{}]",startTm,nowTm);
                isStartOrEndFlg=true;
            }
        }

        if(isStartOrEndFlg){
            log.info("이미 시작 또는 종료된 매치이므로 제외 - 매치 시작 시간[{} {}]/현재 시간[{} {}]",startDt ,startTm,nowDt,nowTm);
        }

//        log.info("매치 진행 전/매치 시작[{} {}]/현재[{} {}]",startDt,startTm,nowDt,nowTm);
        return isStartOrEndFlg;
    }

    public static String plusOneMonth(String yyyyMMdd) {
        LocalDate date = LocalDate.parse(yyyyMMdd, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate afterOneMonth = date.plusMonths(1);
        return afterOneMonth.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
