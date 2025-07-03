package com.sunghyun.football.config.batch.listener.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public abstract class AbstractProcessListener<T,S> implements ItemProcessListener<T,S> {

    @Override
    public abstract void beforeProcess(T item);


    @Override
    public void onProcessError(T item, Exception e) {
        log.warn("Read Error - error={} , item={}", e.getMessage(),item.toString(), e);
    }
}
