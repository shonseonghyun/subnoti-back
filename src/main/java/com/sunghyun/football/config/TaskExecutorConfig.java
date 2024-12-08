package com.sunghyun.football.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {
    private static int CORE_POOL_SIZE= 4;
    private static int MAX_POOL_SIZE = 8;
    private static int QUEUE_CAPACITY = 10;
    private static String THREAD_NAME_PREFIX = "multi-thread-";

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor= new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();

        return executor;
    }
}
