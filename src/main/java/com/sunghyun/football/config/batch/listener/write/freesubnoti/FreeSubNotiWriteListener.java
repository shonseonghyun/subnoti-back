package com.sunghyun.football.config.batch.listener.write.freesubnoti;

import com.sunghyun.football.config.batch.listener.write.AbstractWriteListener;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FreeSubNotiWriteListener extends AbstractWriteListener<FreeSubNoti> {
    @Override
    public void beforeWrite(Chunk<? extends FreeSubNoti> items) {
        for(FreeSubNoti item:items){
            log.info("[{}]Write Item: 노티 요청 번호[{}] 매치명[{}]", Thread.currentThread().getName(), item.getNotiNo(),item.getMatchName());
        }
    }
}
