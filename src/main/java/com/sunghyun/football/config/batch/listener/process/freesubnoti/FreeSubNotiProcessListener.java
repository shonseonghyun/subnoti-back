package com.sunghyun.football.config.batch.listener.process.freesubnoti;

import com.sunghyun.football.config.batch.listener.process.AbstractProcessListener;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FreeSubNotiProcessListener extends AbstractProcessListener<FreeSubNoti,FreeSubNoti> {

    @Override
    public void beforeProcess(FreeSubNoti item) {
        String threadName = Thread.currentThread().getName();
        log.info("[{}]Process Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());

    }
}
