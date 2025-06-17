package com.sunghyun.football.domain.noti.infrastructure;

import com.sunghyun.football.domain.noti.domain.FreeSubNotiHistory;

import java.util.Comparator;

public class FreeSubNotiHistoryComparator  implements Comparator<FreeSubNotiHistory> {
    @Override
    public int compare(FreeSubNotiHistory o1, FreeSubNotiHistory o2) {
        if (o1.getHistoryNo() > o2.getHistoryNo()) {
            return -1;
        } else if (o1.getHistoryNo() < o2.getHistoryNo()) {
            return 1;
        }
        return 0;
    }
}
