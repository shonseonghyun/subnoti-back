package com.sunghyun.football.config.batch.partitioning;

import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import com.sunghyun.football.global.feign.PlabFootBallOpenFeignClient;
import com.sunghyun.football.global.feign.dto.PlabMatchInfoResDto;
import com.sunghyun.football.global.noti.NotiProcessor;
import com.sunghyun.football.global.utils.MatchDateUtils;
import feign.FeignException;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NewFreeSubNotiBatchPartitionConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final int chunkSize=10;
    private final PlabFootBallOpenFeignClient plabFootBallOpenFeignClient;
    private final NotiProcessor notiProcessor;
    private final TaskExecutor taskExecutor;
    private final FreeSubNotiRepository freeSubNotiRepository;

    @Bean
    public Job newFreeSubNotiBatchPartitionJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("freeSubNotiBatchPartitionJob",jobRepository)
                .start(step1Manager(transactionManager,jobRepository))
                .build()
                ;
    }

    @Bean
    public Step step1Manager(PlatformTransactionManager transactionManager, JobRepository jobRepository){
        return new StepBuilder("step1Manager",jobRepository)
                .partitioner("step1",partitioner(null)) //step1에 사용될 partition 구현체를 등록
                .step(step1(transactionManager,jobRepository)) //파티셔닝 될 step을 등록, step1이 등록한 Partition 로직에 따라 서로 다른 StepExecutions를 가진 여러 개로 생성
                .partitionHandler(partitionHandler(transactionManager,jobRepository)) //사용할 partitionHandler 등록
                .listener(partitionListener())
                .build()
                ;
    }

    //매니저(마스터) step이 Worker Step을 어떻게 구성할지 정의
    @Bean
    public PartitionHandler partitionHandler(PlatformTransactionManager transactionManager, JobRepository jobRepository){
        //Local환경에서 Multi Thread로 수행할 수 있도록 TaskExecutorPartitionHandler 할당
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();

        //Worker로 실행할 Step 지정
        //Partitioner가 만들어준 stepExecutions 환경에서 개별적으로 실행
        partitionHandler.setStep(step1(transactionManager,jobRepository));

        partitionHandler.setTaskExecutor(taskExecutor);

        partitionHandler.setGridSize(4);

        return partitionHandler;
    }


    @StepScope
    @Bean
    public Partitioner partitioner(@Value("#{jobParameters['startDt']}") String startDt){
        return new NewNotiNoRangePartitioner(freeSubNotiRepository,startDt);
    }

    @Bean
    public Step step1(PlatformTransactionManager transactionManager, JobRepository jobRepository){
        return new StepBuilder("step1",jobRepository)
                .<FreeSubNotiEntity, FreeSubNotiEntity>chunk(chunkSize,transactionManager)
                .reader(reader(null,null))
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor)
                .listener(partitionListener())
                .listener(readListener())
                .listener(processListener())
                .faultTolerant()
                .skip(FeignException.class)
                .skipLimit(100)
                .build()
                ;
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<FreeSubNotiEntity> reader(
            @Value("#{stepExecutionContext['minNotiNo']}") Long minNotiNo,
            @Value("#{stepExecutionContext['maxNotiNo']}") Long maxNotiNo
    ){
        Map<String,Object> parameterValues = new HashMap<>();
        parameterValues.put("minNotiNo",minNotiNo);
        parameterValues.put("maxNotiNo",maxNotiNo);

        JpaPagingItemReader<FreeSubNotiEntity> reader = new JpaPagingItemReader<>();
        reader.setParameterValues(parameterValues);

//        reader.setQueryString("SELECT m FROM FreeSubNotiEntity m " +
//                "LEFT JOIN FETCH m.freeSubNotiHistories h "+
//                "WHERE m.notiNo BETWEEN :minNotiNo AND :maxNotiNo " +
//                "order by " +
//                "m.startDt desc," +
//                "m.startTm desc," +
//                "m.notiNo desc"
//        );

        reader.setQueryString("SELECT m FROM FreeSubNotiEntity m " +
                                "WHERE m.notiNo BETWEEN :minNotiNo AND :maxNotiNo " +
                                "order by notiNo    "
                );

        reader.setPageSize(chunkSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("customPagingReader");
        reader.setSaveState(false);

        return reader;
    }

    public ItemProcessor<FreeSubNotiEntity, FreeSubNotiEntity> processor(){
        return item->{
            try{
                if(MatchDateUtils.hasAlreadyPassedOfMatch(item.getStartDt(),item.getStartTm())){
                    log.info("매치 [{}] [{}] 처리 패스",item.getMatchName(),item.getMatchNo());
                    return item;
                }

                //플랩 통신
                PlabMatchInfoResDto response = plabFootBallOpenFeignClient.getMatch(item.getMatchNo());
                boolean isManagerSubFree = Boolean.parseBoolean(response.getIs_manager_free());
                boolean isSuperSubFree = Boolean.parseBoolean(response.getIs_super_sub());

                notiProcessor.doNotiProcess(item,isManagerSubFree,isSuperSubFree);

                return item;

            }catch (Exception e){
                log.error("Error processing item with notiNo [{}]: {}", item.getNotiNo(), e.getMessage());
                return null;
            }
        };
    }

    @Bean
    public ItemWriter<FreeSubNotiEntity> writer(){
        JpaItemWriter<FreeSubNotiEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return writer;
    }

    public ItemReadListener<FreeSubNotiEntity> readListener() {
        return new ItemReadListener<FreeSubNotiEntity>() {
            @Override
            public void afterRead(FreeSubNotiEntity item) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]Read Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }
        };
    }

    public ItemProcessListener<FreeSubNotiEntity,FreeSubNotiEntity> processListener() {
        return new ItemProcessListener<FreeSubNotiEntity,FreeSubNotiEntity>() {
            @Override
            public void beforeProcess(FreeSubNotiEntity item) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]Start Process Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }

            @Override
            public void afterProcess(FreeSubNotiEntity item, FreeSubNotiEntity result) {
                String threadName = Thread.currentThread().getName();
                log.info("[{}]End Process Item: 노티 요청 번호[{}] 매치명[{}]", threadName, item.getNotiNo(),item.getMatchName());
            }
        };
    }

    public StepExecutionListener partitionListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                if (stepExecution.getStatus() == BatchStatus.STARTED) {
                    log.info("PartitionStep start! {}", stepExecution.getStepName());
                }
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
                    log.info("PartitionStep end! {}", stepExecution.getStepName());
                }
                return new ExitStatus("stepListener exit");
            }
        };
    }

}
