package com.sunghyun.football.global.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MatchDateUtils {
    public static String getDtStr(Date date){
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        return dtFormat.format(date);
    }

    public static String getTmStr(Date date){
        SimpleDateFormat tmFormat = new SimpleDateFormat("HHmm");
        return tmFormat.format(date);
    }

    public static String getTmAfterHours(Date date,int hours){
        SimpleDateFormat tmFormat = new SimpleDateFormat("HHmm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,hours);

        return tmFormat.format(calendar.getTime());
    }
}
