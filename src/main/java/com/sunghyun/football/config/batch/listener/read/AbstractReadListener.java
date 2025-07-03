package com.sunghyun.football.config.batch.listener.read;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

@Slf4j
public abstract class AbstractReadListener<T> implements ItemReadListener<T> {
    @Override
    public abstract void afterRead(T item);

    @Override
    public void onReadError(Exception ex) {
        log.warn("Read Error - error={}", ex.getMessage(), ex);
    }
}
