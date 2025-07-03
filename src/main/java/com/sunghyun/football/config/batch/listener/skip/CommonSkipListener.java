package com.sunghyun.football.config.batch.listener.skip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommonSkipListener<T,S> implements SkipListener<T,S> {
    @Override
    public void onSkipInRead(Throwable t) {
        log.warn("Read skip - error={}", t.getMessage(), t);
    }

    @Override
    public void onSkipInWrite(S item, Throwable t) {
        log.warn("Write skip - item={}, error={}", item, t.getMessage(), t);
    }

    @Override
    public void onSkipInProcess(T item, Throwable t) {
        log.warn("Process skip - item={}, error={}", item, t.getMessage(), t);
    }
}
