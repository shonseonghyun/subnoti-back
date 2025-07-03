package com.sunghyun.football.config.batch.listener.write;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

@Slf4j
public abstract class AbstractWriteListener<T> implements ItemWriteListener<T> {
    @Override
    public abstract void beforeWrite(Chunk<? extends T> items);



    @Override
    public void onWriteError(Exception exception, Chunk<? extends T> items) {
        log.warn("Write Error - error={}", exception.getMessage(), exception);
    }
}
