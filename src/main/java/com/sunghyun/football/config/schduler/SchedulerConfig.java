package com.sunghyun.football.config.schduler;

import com.sunghyun.football.config.batch.FreeSubNotiRegBatchMultiThreadConfig;
import com.sunghyun.football.domain.match.application.MatchApplication;
import com.sunghyun.football.global.utils.MatchDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerConfig {
    private final JobLauncher jobLauncher;
    private final PlatformTransactionManager manager;
    private final JobRepository jobRepository;
    private final FreeSubNotiRegBatchMultiThreadConfig freeSubNotiRegBatchMultiThreadConfig;
    private final MatchApplication matchApplication;

    @Scheduled(cron = "0/30 * * * * *" , zone = "Asia/Seoul") //30초마다
    public void freesubNotiRegScheduler() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("Frees 노티 알림 스케줄러 start");
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기

        JobParameters jobParameters= new JobParametersBuilder()
                .addString("nowDt", MatchDateUtils.getNowDtStr())
                .addLong("time",new Date().getTime()) //여러번 돌수 있게 세팅
                .toJobParameters();

        jobLauncher.run(freeSubNotiRegBatchMultiThreadConfig.freeSubNotiRegMultiThreadJob(jobRepository, manager),jobParameters);

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        log.info("소요 시간(m) : {}초",secDiffTime);
        log.info("프리서브 노티 알림 스케줄러 end");
    }

    @Scheduled(fixedRate = 1000*60*60)
    public void syncViewCountsScheduler(){
        log.info("조회수 동기화 스케줄러 start");
        matchApplication.syncViewCounts();
        log.info("조회수 동기화 스케줄러 end");

    }
}
