package com.sunghyun.football.config.batch.listener.read.freesubnoti;

import com.sunghyun.football.config.batch.listener.read.AbstractReadListener;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FreeSubNotiReadListener extends AbstractReadListener<FreeSubNoti> {

    @Override
    public void afterRead(FreeSubNoti item) {
        String threadName = Thread.currentThread().getName();
        log.info("[{}]Read Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
    }
}
