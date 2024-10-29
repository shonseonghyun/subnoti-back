package com.sunghyun.football.domain.noti.infrastructure;

import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiHistoryEntity;

import java.util.Comparator;

public class FreeSubNotiHistoryComparator  implements Comparator<FreeSubNotiHistoryEntity> {
    @Override
    public int compare(FreeSubNotiHistoryEntity o1, FreeSubNotiHistoryEntity o2) {
        if (o1.getHistoryNo() > o2.getHistoryNo()) {
            return -1;
        } else if (o1.getHistoryNo() < o2.getHistoryNo()) {
            return 1;
        }
        return 0;
    }
}
